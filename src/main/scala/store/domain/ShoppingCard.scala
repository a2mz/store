package store.domain

import java.util.UUID
import cats.syntax.option._
case class ShoppingCard(userName: String, items: Map[ProductItem, Int], orderId: Option[UUID])

object ShoppingCard {
  def apply(userName: String, items: Map[ProductItem, Int]): ShoppingCard = new ShoppingCard(userName, items, none)
}
