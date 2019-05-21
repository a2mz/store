package store.utils

import play.api.libs.json._

import scala.collection.mutable

object FormatOps {

  implicit val writesUnit: Writes[Unit] = (o: Unit) => "ok".toJson

  implicit class PimpedAny[T](val any: T) extends AnyVal {
    def toJson(implicit writer: Format[T]): JsValue = writer.writes(any)
    def toJsonObject(implicit writer: OFormat[T]): JsObject = writer.writes(any)
  }

  implicit def mapFormat[K: Format, V: Format]: OFormat[Map[K, V]] = new OFormat[Map[K, V]] {
    def writes(m: Map[K, V]) = JsObject {
      m.map {
        case (name, value) =>
          name.toJson match {
            case JsString(x) => x -> value.toJson
            case unx => throw new IllegalArgumentException("Map key must be formatted as JsString, not '" + unx + "'")
          }
      }
    }
    def reads(jsValue: JsValue): JsResult[Map[K, V]] = jsValue match {
      case jsObject: JsObject =>
        var builderAsResult: JsResult[mutable.Builder[(K, V), Map[K, V]]] = JsSuccess(Map.newBuilder[K, V])
        builderAsResult = jsObject.fields.foldLeft(builderAsResult) {
          case (builder, (name, value)) =>
            for {
              b <- builder
              k <- JsString(name).validate[K]
              v <- value.validate[V]
            } yield b += ((k, v))
        }
        builderAsResult.map(_.result())

      case x => JsError("Expected Map as JsObject, but got " + x)
    }
  }
}
