package store.repository.user

import cats.Applicative
import cats.implicits._
import store.domain.User

import scala.collection.concurrent.TrieMap

class UserRepository[F[_]: Applicative] extends UserRepositoryLike[F] {
  private val dataBase                  = new TrieMap[String, User]
  override def add(user: User): F[User] = dataBase.update(user.userName, user).pure[F].map(_ => user)
  override def get(userName: String): F[Option[User]] =
    dataBase.get(userName).pure[F]
  override def delete(userName: String): F[Option[User]] = dataBase.remove(userName).pure[F]
}

object UserRepository {
  def apply[F[_]: Applicative](): UserRepositoryLike[F] = new UserRepository[F]()
}
