package store.services

import java.security.MessageDigest
import java.util.UUID

import store.domain.User
import store.repository.user.UserRepositoryLike
import store.services.UserService.UserSession

class UserService[F[_]](userRepo: UserRepositoryLike[F]) {


def addUser(user:User): F[User] = ???
  def deleteUser(userId:UUID): F[Unit] = ???
def authUser(user:User): F[UserSession] = ???
}

object UserService {
type UserSession = UUID
  def apply[F[_]](userRepo: UserRepositoryLike[F]): UserService[F] =
    new UserService(userRepo)

  sealed trait UserServiceError
  case object UserNotFound extends UserServiceError
  case object UserAlreadyExist extends UserServiceError


  type Hash = String
  def md5(str: String): Hash = {
    MessageDigest.getInstance("MD5").digest(str.getBytes()).map("%02x".format(_)).mkString
  }
}


