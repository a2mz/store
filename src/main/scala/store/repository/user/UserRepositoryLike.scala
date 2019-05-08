package store.repository.user

import java.util.UUID

import store.domain.User

trait UserRepositoryLike[F[_]] {
  def add(user: User): F[User]
  def delete(userName: String): F[Option[User]]
}
