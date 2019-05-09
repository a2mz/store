package store.services

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._
import store.domain.ProductItem
import store.repository.productFeed.ProductFeedRepositoryLike
import store.services.ProductFeedService._

class ProductFeedService[F[_]: Applicative](productRepo: ProductFeedRepositoryLike[F]) {

  def addProduct(product: ProductItem): F[ProductItem] = productRepo.add(product)

  def findProduct(id: UUID): EitherT[F, ProductServiceError, ProductItem] =
    EitherT.fromOptionF(productRepo.get(id), ProductNotFound)

  def findAllProducts: F[List[ProductItem]] = productRepo.getAll

  def deleteProduct(id: UUID, quantity: Int): F[Unit] =
    productRepo.delete(id, quantity).as(())
}

object ProductFeedService {
  def apply[F[_]: Applicative](productRepo: ProductFeedRepositoryLike[F]): ProductFeedService[F] =
    new ProductFeedService(productRepo)

  sealed trait ProductServiceError
  case object ProductNotFound extends ProductServiceError
}
