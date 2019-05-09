package store.services

import cats._
import cats.data._
import cats.implicits._
import store.domain.User
import store.repository.productFeed.ProductFeedRepository
import store.repository.user.UserRepositoryLike
import store.services.UserService.{UserAlreadyExist, UserNotFound, UserServiceError}

class OrderService[F[_]: Monad](
    userRepo: UserRepositoryLike[F],
    productFeedRepository: ProductFeedRepository[F]
) {

  def addUser(user: User): EitherT[F, UserServiceError, User] =
    EitherT(userRepo.get(user.userName).flatMap {
      case None => userRepo.add(user).map(_.asRight[UserServiceError])
      case _    => (UserAlreadyExist: UserServiceError).asLeft[User].pure[F]
    })
  def deleteUser(userName: String): EitherT[F, UserServiceError, Unit] =
    EitherT(userRepo.delete(userName).map(_.fold((UserNotFound: UserServiceError).asLeft[Unit])(_ => ().asRight[UserServiceError])))
}

object OrderService {
  def apply[F[_]: Monad](
      userRepo: UserRepositoryLike[F],
      productFeedRepository: ProductFeedRepository[F]
  ): OrderService[F] = new OrderService[F](userRepo, productFeedRepository)
}
