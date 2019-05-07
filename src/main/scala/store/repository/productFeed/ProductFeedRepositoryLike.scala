package store.repository.productFeed

import java.util.UUID

import store.domain.ProductItem

trait ProductFeedRepositoryLike[F[_]] {
  def get(productId: UUID): F[Option[ProductItem]]
  def getAll: F[List[ProductItem]]
  def add(product: ProductItem): F[ProductItem]
  def delete(productId: UUID, quantity:Int): F[Option[ProductItem]]
}
