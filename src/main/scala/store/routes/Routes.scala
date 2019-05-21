package store.routes
import akka.http.scaladsl.server.Directives.{pathEnd, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import cats.implicits._
import store.domain.{ProductItem, User}
import store.services.{OrderService, ProductFeedService, UserService}
import store.utils.FormatOps.writesUnit

import scala.concurrent.{ExecutionContext, Future}
object Routes extends RoutesCallbacks {

  def apply(userService: UserService[Future], productService: ProductFeedService[Future], productCardService: OrderService[Future])(
      implicit ex: ExecutionContext) =
    userManagement("user", userService) ~
      productManagement("product", productService) ~
      shoppingCardManagement("card", productCardService)

  private def userManagement(rootPath: String, service: UserService[Future]) = {

    def addUser: Route = {
      post {
        pathPrefix(rootPath) {
          pathEnd {
            entity(as[User]) { user =>
              actionCreate {
                service.addUser(user).value
              }
            }
          }
        }
      }
    }

    def deleteUser: Route = {
      delete {
        pathPrefix(rootPath) {
          pathPrefix(Segment) { userName =>
            actionDelete {
              service.deleteUser(userName).value
            }
          }
        }
      }
    }

    addUser ~ deleteUser

  }

  private def productManagement(rootPath: String, service: ProductFeedService[Future])(implicit ex: ExecutionContext) = {

    def addProduct: Route = {
      post {
        pathPrefix(rootPath) {
          pathEnd {
            entity(as[ProductItem]) { product =>
              actionCreate {
                service.addProduct(product).map(_.asRight)
              }
            }
          }
        }
      }
    }

    def findProduct: Route = {
      get {
        pathPrefix(rootPath) {
          pathEnd {
            pathPrefix(JavaUUID) { id =>
              actionFind {
                service.findProduct(id).value
              }
            }
          }
        }
      }
    }

    def findAllProducts: Route = {
      get {
        pathPrefix(rootPath) {
          pathEnd {
            actionFindAll {
              service.findAllProducts
            }
          }
        }
      }
    }

    def deleteProduct: Route = {
      delete {
        pathPrefix(rootPath) {
          pathPrefix(JavaUUID) { id =>
            pathPrefix(Segment) { quantity =>
              actionDelete {
                service.deleteProduct(id, quantity.toInt).map(_.asRight)
              }
            }
          }
        }
      }
    }
    addProduct ~ findProduct ~ findAllProducts ~ deleteProduct
  }

  private def shoppingCardManagement(rootPath: String, service: OrderService[Future]) = {

    def printShoppingCardProducts: Route = {
      get {
        pathPrefix(rootPath) {
          pathPrefix(Segment) { userName =>
            actionFind {
              service.getShoppingCard(userName).value
            }
          }
        }
      }
    }

    def addIted: Route = {
      post {
        pathPrefix(rootPath) {
          pathPrefix(Segment) { userName =>
            entity(as[ProductItem]) { item =>
              actionCreate {
                service.addItemToShoppingCard(userName, item).value
              }
            }
          }
        }
      }
    }

    def clearShoppingCard: Route = {
      delete {
        pathPrefix(rootPath) {
          pathPrefix(Segment) { userName =>
            actionDelete {
              service.clearShoppingCard(userName).value
            }
          }
        }
      }
    }

    def checkout: Route = {
      post {
        pathPrefix(rootPath) {
          pathPrefix(Segment) { userName =>
            actionCreate {
              service.checkout(userName).value
            }
          }
        }
      }
    }

    printShoppingCardProducts ~ addIted ~ clearShoppingCard ~ checkout

  }

}
