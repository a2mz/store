package store.domain

case class User(userName: String, password: String)

object User {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._
  implicit val userFormat = (
    (__ \ "userName").format[String] and
      (__ \ "password").format[String]
  )(User.apply, unlift(User.unapply))
}
