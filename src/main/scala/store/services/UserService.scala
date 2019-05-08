package store.services

import java.util.UUID

import cats.Applicative
import cats.data._
import cats.implicits._
import store.domain.User
import store.repository.user.UserRepositoryLike
import store.services.UserService.{UserNotFound, UserServiceError}

class UserService[F[_]: Applicative](userRepo: UserRepositoryLike[F]) {
  def addUser(user: User): F[User] = userRepo.add(user)
  def deleteUser(userName: String): EitherT[F, UserServiceError, Unit] =
    EitherT(userRepo.delete(userName).map(_.fold((UserNotFound: UserServiceError).asLeft[Unit])(_ => ().asRight[UserServiceError])))
}

object UserService {
  type UserSession = UUID
  def apply[F[_]](userRepo: UserRepositoryLike[F]): UserService[F] =
    new UserService(userRepo)

  sealed trait UserServiceError
  case object UserNotFound extends UserServiceError

}
