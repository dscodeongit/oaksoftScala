package com.oaksoft.utils.mongodb

import scala.collection.concurrent.{ Map, TrieMap }
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.mutable.Document
import org.apache.logging.log4j.LogManager
import com.oaksoft.utils.mongodb.MongoDataStore._

class MongoDataStore(mongoDbUrls: String, dbName: String) {
   val mongoDB = MongoDB(mongoDbUrls, dbName)
   val dbCollections: Map[String, MongoDBCollection] = new TrieMap

   def newCollection(collectionName: String, ttlField: String, ttlInSecs: Long) = {
      val coll = MongoDBCollection(collectionName, ttlField, ttlInSecs, mongoDB)
      dbCollections.putIfAbsent(collectionName, coll)
   }

   def newCollection(collectionName: String, ttlInSecs: Long) = {
      val coll = MongoDBCollection(collectionName, ttlInSecs, mongoDB)
      dbCollections.putIfAbsent(collectionName, coll)
   }

   def newCollection(collectionName: String) = {
      val coll = MongoDBCollection(collectionName, mongoDB)
      dbCollections.putIfAbsent(collectionName, coll)
   }

   def getCollection(collName: String): MongoCollection[Document] = {
      dbCollections.get(collName) match {
         case None => {
            logger.warn(s"No collection with name: $collName, must initialize the collection before use!")
            null
         }
         case c : Some[MongoDBCollection] => {
            c.get.getCollection
         }
      }
   }
   
   def close ={
      mongoDB.close
   }
}

object MongoDataStore {
   val logger = LogManager.getLogger(MongoDataStore.getClass)
   def apply(mongoDbUrls: String, dbName: String) = {
      new MongoDataStore(mongoDbUrls, dbName)
   }
}