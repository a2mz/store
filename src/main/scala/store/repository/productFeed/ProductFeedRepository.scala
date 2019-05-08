package store.repository.productFeed

import java.util.UUID

import cats._
import cats.implicits._
import store.domain.ProductItem

import scala.collection.concurrent.TrieMap

class ProductFeedRepository[F[_]: Applicative] extends ProductFeedRepositoryLike[F] {

  private val dataBase = new TrieMap[UUID, ProductItem]

  val plus: Int => Int => Int  = (a: Int) => (b: Int) => a + b
  val minus: Int => Int => Int = (a: Int) => (b: Int) => b - a

  private def updateByKey(item: ProductItem, quantityOperation: Int => Int => Int): ProductItem = {
    val key = item.productId
    synchronized {
      dataBase.get(key) match {
        case Some(data) =>
          val newValue = item.copy(quantity = quantityOperation(item.quantity)(data.quantity))
          dataBase.update(key, newValue)
          newValue
        case None =>
          dataBase.update(key, item)
          item
      }
    }
  }

  override def get(productId: UUID): F[Option[ProductItem]] = dataBase.get(productId).pure[F]

  override def add(product: ProductItem): F[ProductItem] = updateByKey(product, plus).pure[F]

  override def delete(productId: UUID, quantity: Int): F[Option[ProductItem]] =
    dataBase.get(productId).map(item => updateByKey(item.copy(quantity = quantity), minus)).pure[F]

  override def getAll: F[List[ProductItem]] = ???
}

object ProductFeedRepository {
  def apply[F[_]: Applicative](): ProductFeedRepository[F] = new ProductFeedRepository[F]()
}
