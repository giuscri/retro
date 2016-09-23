package mailo.http

import mailo.Attachment

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials, RawHeader}
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.unmarshalling._

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.actor.ActorSystem

import scala.concurrent.Future

import com.typesafe.config.{ ConfigFactory, Config }

import scalaz.\/
import scalaz.syntax.either._

import akka.http.scaladsl.model.ContentType
import akka.util.ByteString

class MailgunClient(implicit
  system: ActorSystem,
  materializer: ActorMaterializer,
  conf: Config = ConfigFactory.load()
) extends MailClient {
  import mailo.MailError
  import MailClientError._
  import mailo.MailRefinedContent._
  import mailo.MailResponse

  private[this] case class MailgunConfig(key: String, uri: String)
  private[this] val mailgunConfig = MailgunConfig(
    key = conf.getString("mailgun.key"),
    uri = conf.getString("mailgun.uri")
  )

  def send(
    to: String,
    from: String,
    subject: String,
    content: MailRefinedContent,
    attachments: List[Attachment],
    tags: List[String]
  )(implicit
    executionContext: scala.concurrent.ExecutionContext
  ): Future[MailError \/ MailResponse] = {
    import de.heikoseeberger.akkahttpcirce.CirceSupport._
    import io.circe.generic.auto._

    val auth = Authorization(BasicHttpCredentials("api", mailgunConfig.key))

    for {
      entity <- entity(
        from = from,
        to = to,
        subject = subject,
        content = content,
        attachments = attachments,
        tags = tags
      )
      request = HttpRequest(
        method = HttpMethods.POST,
        uri = s"${mailgunConfig.uri}/messages",
        headers = List(auth),
        entity = entity
      )
      response <- Http().singleRequest(request)
      result <- response.status.intValue match {
        case 200 => Unmarshal(response.entity).to[MailResponse] map (_.right[MailError])
        case 400 => Future(BadRequest.left[MailResponse])
        case 401 => Future(Unauthorized.left[MailResponse])
        case 402 => Future(RequestFailed.left[MailResponse])
        case 404 => Future(NotFound.left[MailResponse])
        case 500 | 502 | 503 | 504 => Future(ServerError.left[MailResponse])
        case _ => Future(UnknownCode.left[MailResponse])
      }
    } yield result
  }

  private[this] def attachmentForm(name: String, `type`: ContentType, content: String, transferEncoding: Option[String] = None) = {
    Multipart.FormData.BodyPart.Strict(
      name = "attachment",
      entity = HttpEntity(`type`, ByteString(content)),
      additionalDispositionParams = Map("filename" -> name),
      additionalHeaders = transferEncoding match {
                            case Some(e) => List(RawHeader("Content-Transfer-Encoding", e))
                            case None => Nil
                          }
    )
  }

  private[this] def entity(
    from: String,
    to: String,
    subject: String,
    content: MailRefinedContent,
    attachments: List[Attachment],
    tags: List[String]
  )(implicit
    executionCon: scala.concurrent.ExecutionContext
  ): Future[RequestEntity] = {
    import mailo.MailRefinedContent._
    val tagsForm = tags map (Multipart.FormData.BodyPart.Strict("o:tag", _))

    val contentForm = content match {
      case HTMLContent(html) => Multipart.FormData.BodyPart.Strict("html", html)
      case TEXTContent(text) => Multipart.FormData.BodyPart.Strict("text", text)
    }

    val attachmentsForm = attachments map (attachment => attachmentForm(attachment.name, attachment.`type`, attachment.content, attachment.transferEncoding))

    val multipartForm = Multipart.FormData(Source(List(
      Multipart.FormData.BodyPart.Strict("from", from),
      Multipart.FormData.BodyPart.Strict("to", to),
      Multipart.FormData.BodyPart.Strict("subject", subject)
    ) ++ tagsForm ++ attachmentsForm :+ contentForm ))

    Marshal(multipartForm).to[RequestEntity]
  }
}
