package store.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{complete, onSuccess}
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.Writes

import scala.concurrent.Future

trait RoutesCallbacks extends PlayJsonSupport with LazyLogging {


  def actionCreate[T, S](block: => Future[Either[S, T]])(implicit w: Writes[T], we: Writes[S]): Route = {
    onSuccess(block) {
      case Right(data) => complete((Created, data))
      case Left(err) =>
        logger.debug(s"Failed to create or update entity reason=$err.")
        complete((BadRequest, err))
    }
  }

  def actionDelete[S](block: => Future[Either[S, Unit]])(implicit we: Writes[S]): Route = {
    onSuccess(block) {
      case Right(data) => complete(OK)
      case Left(err) =>
        logger.debug(s"Failed to delete entity reason=$err.")
        complete((BadRequest, err))
    }
  }

  def actionFind[T](block: => Future[Option[T]])(implicit w: Writes[T]): Route = {
    onSuccess(block) {
      case Some(data) => complete((OK, data))
      case None =>
        logger.debug("Nothing was found by request")
        complete(akka.http.scaladsl.model.StatusCodes.NotFound)
    }
  }

  def actionFind[T, S](block: => Future[Either[S, T]])(implicit w: Writes[T], we: Writes[S]): Route = {
    onSuccess(block) {
      case Right(data) => complete((Created, data))
      case Left(err) =>
        logger.debug(s"Failed to find entity reason=$err.")
        complete((BadRequest, err))
    }
  }

  def actionFindAll[T](block: => Future[Iterable[T]])(implicit w: Writes[T]): Route = {
    onSuccess(block) { entities: Iterable[T] =>
      complete((OK, entities))
    }
  }

}
