package store.repository.order

import java.util.UUID

import cats.{Applicative, Monad}
import store.domain.{ProductItem, ShoppingCard, User}
import store.repository.user.UserRepositoryLike
import cats._
import cats.data._
import cats.implicits._

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

class OrderRepository[F[_]: Monad] extends OrderRepositoryLike[F] {
  private val dataBase = new TrieMap[String, ShoppingCard]

  override def getShoppingCard(userName: String): F[Option[ShoppingCard]] = dataBase.get(userName).pure[F]
  override def addItem(userName: String, item: ProductItem): F[ShoppingCard] = {
    val updatedShoppingCard = ShoppingCard(userName, Map(item -> dataBase.get(userName).fold(1)(_.items.get(item).fold(1)(_ + 1))))
    dataBase.update(userName, updatedShoppingCard).pure[F].map(_ => updatedShoppingCard)
  }

  override def deleteShoppingCard(userName: String): F[Unit] = dataBase.remove(userName).pure[F].map(_ => ())
  override def checkout(userName: String): F[UUID]           = ???

}

object OrderRepository {
  def apply[F[_]: Monad](): OrderRepositoryLike[F] = new OrderRepository[F]()
}
