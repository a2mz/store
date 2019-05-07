package store.services

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._
import store.domain.ProductItem
import store.repository.productFeed.ProductFeedRepositoryLike
import store.services.ProductFeedService._

class ProductFeedService[F[_]](orderRepo: ProductFeedRepositoryLike[F]) {

  def addProduct(product: ProductItem): F[ProductItem] = orderRepo.add(product)

  def findProduct(id: UUID)(implicit M: Monad[F]): EitherT[F, ProductServiceError, ProductItem] =
    EitherT.fromOptionF(orderRepo.get(id), ProductNotFound)

  def findAllProducts(implicit M: Monad[F]): F[List[ProductItem]] = orderRepo.getAll

  def deleteProduct(id: UUID, quantity: Int)(implicit M: Monad[F]): F[Unit] =
    orderRepo.delete(id, quantity).as(())
}

object ProductFeedService {
  def apply[F[_]](orderRepo: ProductFeedRepositoryLike[F]): ProductFeedService[F] =
    new ProductFeedService(orderRepo)

  sealed trait ProductServiceError
  case object ProductNotFound extends ProductServiceError
}
