package store.domain

import java.util.UUID

import cats.syntax.option._
case class ShoppingCard(userName: String, items: Map[ProductItem, Int], orderId: Option[UUID])

object ShoppingCard {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._
  import store.utils.FormatOps._
  implicit val productItemFormat = (
    (__ \ "userName").format[String] and
      (__ \ "items").format[Map[ProductItem, Int]] and
      (__ \ "orderId").formatNullable[UUID]
  )(ShoppingCard.apply, unlift(ShoppingCard.unapply))

  def apply(userName: String, items: Map[ProductItem, Int]): ShoppingCard = new ShoppingCard(userName, items, none)
}

