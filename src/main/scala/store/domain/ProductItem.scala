package store.domain

import java.util.UUID

import enumeratum._

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
}

case class ProductItem(productId: UUID, minorCurrencyValue: Int, currency: Currency, desc:String) {
  def toMajorCurrencyValue: Long = minorCurrencyValue * currency.multipliers
}

object ProductItem {
  def apply(minorCurrencyValue: Int, currency: Currency, desc: String): ProductItem = new ProductItem(UUID.randomUUID, minorCurrencyValue, currency, desc)
}
