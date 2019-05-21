package store.services

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._
import store.domain.{ProductItem, ShoppingCard}
import store.repository.order.OrderRepositoryLike
import store.services.UserService.UserServiceError

class OrderService[F[_]: Monad](
    orderRepo: OrderRepositoryLike[F]
) {

  def getShoppingCard(userName: String): EitherT[F, UserServiceError, Option[ShoppingCard]] =
    EitherT(orderRepo.getShoppingCard(userName).map(_.asRight))

  def clearShoppingCard(userName: String): EitherT[F, UserServiceError,Unit] =
    EitherT(orderRepo.deleteShoppingCard(userName).map(_.asRight))

 def addItemToShoppingCard(userName: String, item: ProductItem): EitherT[F, UserServiceError, ShoppingCard] =
    EitherT(orderRepo.addItem(userName,item ).map(_.asRight))

  def checkout(userName: String): EitherT[F, UserServiceError, Option[UUID]] =
    EitherT(orderRepo.checkout(userName).map(_.asRight))
  //todo: Need to real checkout with constraints, but not today ...

}

object OrderService {
  def apply[F[_]: Monad](
    orderRepo: OrderRepositoryLike[F]
  ): OrderService[F] = new OrderService[F](orderRepo)
}
