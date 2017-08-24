package com.oaksoft.utils.mongodb

import org.mongodb.scala.model.Filters._
import com.oaksoft.utils.mongodb.MongoDBCollection._
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import org.bson.conversions.Bson
import com.oaksoft.utils.mongodb.ObsHelper._

object MongoDbTool{
  
   def find(collection: MongoDBCollection, filter: Bson): java.util.List[String] = {
      collection.getCollection.find(filter).results.map(_.toJson()).asJava
   }
}