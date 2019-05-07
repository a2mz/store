package store

import cats._
import store.repository.productFeed.ProductFeedRepository
import store.services.ProductFeedService

object Main extends App {

  val productFeedService = ProductFeedService(ProductFeedRepository[Id]())

}

