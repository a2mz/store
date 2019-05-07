package store.repository.productFeed

import java.util.UUID

import cats._
import cats.implicits._
import store.domain.ProductItem

import scala.collection.concurrent.TrieMap

class ProductFeedRepository[F[_]: Applicative] extends ProductFeedRepositoryLike[F] {

  private val dataBase = new TrieMap[UUID, ProductItem]

  override def get(productId: UUID): F[Option[ProductItem]] = dataBase.get(productId).pure[F]

  override def create(product: ProductItem): F[ProductItem] = {
    dataBase.update(product.productId, product)
    product.pure[F]
  }

  override def delete(productId: UUID): F[Option[ProductItem]] = dataBase.remove(productId).pure[F]
}

object ProductFeedRepository {
  def apply[F[_]: Applicative]() = new ProductFeedRepository[F]()
}
