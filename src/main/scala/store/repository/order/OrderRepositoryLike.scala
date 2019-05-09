package store.repository.order

import java.util.UUID

import store.domain.{ProductItem, ShoppingCard, User}

trait OrderRepositoryLike[F[_]] {
  def getShoppingCard(userName: String): F[Option[ShoppingCard]]
  def addItem(userName: String, item: ProductItem): F[ShoppingCard]
  def deleteShoppingCard(userName: String): F[Unit]
  def checkout(userName: String): F[Option[UUID]]
}
