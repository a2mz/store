package store

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import store.repository.order.OrderRepository
import store.repository.productFeed.ProductFeedRepository
import store.repository.user.UserRepository
import store.routes.Routes
import store.services.{OrderService, ProductFeedService, UserService}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Main extends App with LazyLogging{

  implicit val system: ActorSystem = ActorSystem("store")
  import store.Main.system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

  val userService: UserService[Future] = UserService(UserRepository[Future]())
  val productFeedService = ProductFeedService(ProductFeedRepository[Future]())
  val orderService = OrderService(OrderRepository[Future]())

  for {
    route <- Future.fromTry(Try(Route.seal(Routes(userService,productFeedService,orderService))))
    binding <- Http().bindAndHandle(route, "0.0.0.0", 1234).andThen {
      case Success(binding) => logger.info(s"Endpoint successfully bound to $binding")
      case Failure(ex) => logger.error(s"Endpoint binding FAILED with error: $ex", ex)
    }
  } yield Some(binding)


}

