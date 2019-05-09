package store.repository.order

import java.util.UUID

import cats.Monad
import cats.implicits._
import store.domain.{ProductItem, ShoppingCard}

import scala.collection.concurrent.TrieMap

class OrderRepository[F[_]: Monad]() extends OrderRepositoryLike[F] {
  private val dataBase = new TrieMap[String, ShoppingCard]

  override def getShoppingCard(userName: String): F[Option[ShoppingCard]] = dataBase.get(userName).pure[F]

  override def addItem(userName: String, item: ProductItem): F[ShoppingCard] = {
    val updatedShoppingCard = ShoppingCard(userName, Map(item -> dataBase.get(userName).fold(1)(_.items.get(item).fold(1)(_ + 1))))
    dataBase.update(userName, updatedShoppingCard).pure[F].map(_ => updatedShoppingCard)
  }

  override def deleteShoppingCard(userName: String): F[Unit] = dataBase.remove(userName).pure[F].map(_ => ())

  override def checkout(userName: String): F[Option[UUID]] = {
    val newOrderId = UUID.randomUUID
    getShoppingCard(userName).map({
      case Some(sc) =>
        dataBase.update(userName, sc.copy(orderId = newOrderId.some))
        newOrderId.some
      case _ => none
    })
  }
}

object OrderRepository {
  def apply[F[_]: Monad](): OrderRepositoryLike[F] = new OrderRepository[F]()
}
