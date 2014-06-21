package filters

import play.api.Logger
import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.Constant._

object LoggingFilter extends Filter {
  def apply(nextFilter: (RequestHeader) => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      // Logger.info(s"${requestHeader.method} ${requestHeader.uri} " +
      //   s"took ${requestTime}ms and returned ${result.header.status}")
      //Logger.info(s"############result: $result")
      //Logger.info(s"%%%%%%%%%%%%%%%%%%%%%% ${requestHeader.session}, ${requestHeader.acceptLanguages}")
      result.withHeaders("Request-Time" -> requestTime.toString)
        .withCookies(Cookie(cookieNameTimestamp, endTime.toString))
    }
  }

}
