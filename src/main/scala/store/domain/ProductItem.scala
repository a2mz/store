package store.domain

import java.util.UUID

import enumeratum._
import play.api.libs.json._

import scala.collection.immutable

sealed trait Currency extends EnumEntry {
  def multipliers: Int
}

object Currency extends Enum[Currency] {

  case object USD extends Currency {
    override def multipliers: Int = 100
  }
  case object EUR extends Currency {
    override def multipliers: Int = 100
  }
  case object JPY extends Currency {
    override def multipliers: Int = 1
  }

  override def values: immutable.IndexedSeq[Currency] = findValues

  implicit val currencyFormat: Format[Currency] = new Format[Currency] {
    override def writes(x: Currency): JsValue = JsString(x.entryName)
    override def reads(json: JsValue): JsResult[Currency] = json match {
      case JsString(string) =>
        Currency.withNameOption(string).map(JsSuccess(_)).getOrElse(JsError("Unknown Currency type."))
      case _ => JsError(s"Wrong Currency type format.")
    }
  }

}

case class ProductItem(productId: UUID, minorCurrencyValue: Int, currency: Currency, desc: String, quantity: Int) {
  def toMajorCurrencyValue: Long = minorCurrencyValue * currency.multipliers
}

object ProductItem {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._
  implicit val productItemFormat = (
    (__ \ "productId").format[UUID] and
      (__ \ "minorCurrencyValue").format[Int] and
      (__ \ "currency").format[Currency] and
      (__ \ "desc").format[String] and
      (__ \ "quantity").format[Int]
  )(ProductItem.apply, unlift(ProductItem.unapply))
}
