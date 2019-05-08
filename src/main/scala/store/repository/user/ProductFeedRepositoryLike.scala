package store.repository.user

import java.util.UUID

import store.domain.User

trait UserRepositoryLike[F[_]] {
  def update(user:User): F[User]
  def get(userName: String): F[Option[User]]
  def add(user:User): F[User]
  def delete(userId: UUID): F[Option[User]]
}
