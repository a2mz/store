package store.repository.productFeed

import java.util.UUID

import store.domain.ProductItem

trait ProductFeedRepositoryLike[F[_]] {
  def get(productId: UUID): F[Option[ProductItem]]
  def create(product: ProductItem): F[ProductItem]
  def delete(productId: UUID): F[Option[ProductItem]]
}
