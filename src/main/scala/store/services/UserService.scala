package store.services

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._
import store.domain.User
import store.repository.user.UserRepositoryLike
import store.services.UserService.{UserAlreadyExist, UserNotFound, UserServiceError}

class UserService[F[_]: Monad](userRepo: UserRepositoryLike[F]) {
  def addUser(user: User): EitherT[F, UserServiceError, User] =
    EitherT(userRepo.get(user.userName).flatMap {
      case None => userRepo.add(user).map(_.asRight[UserServiceError])
      case _    => (UserAlreadyExist: UserServiceError).asLeft[User].pure[F]
    })
  def deleteUser(userName: String): EitherT[F, UserServiceError, Unit] =
    EitherT(userRepo.delete(userName).map(_.fold((UserNotFound: UserServiceError).asLeft[Unit])(_ => ().asRight[UserServiceError])))
}

object UserService {
  type UserSession = UUID
  def apply[F[_]: Monad](userRepo: UserRepositoryLike[F]): UserService[F] =
    new UserService(userRepo)

  sealed trait UserServiceError
  case object UserNotFound     extends UserServiceError
  case object UserAlreadyExist extends UserServiceError

}
