package com.oaksoft.utils.mongodb


import com.mongodb.connection._
import com.mongodb.ServerAddress
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import com.mongodb.async.client.MongoClientSettings
import org.mongodb.scala.{ MongoDatabase, MongoClient, MongoCollection }
import org.mongodb.scala.bson.collection.mutable.Document
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.bson.codecs.DocumentCodecProvider


class MongoDB(mongoDbUrls: String, dbName: String) {
   var dbHosts = new ListBuffer[ServerAddress]
   mongoDbUrls.split(",|;").foreach(dbHosts += new ServerAddress(_))
   val clusterSettings: ClusterSettings = ClusterSettings.builder().hosts(dbHosts.toList).build()
   val codecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(DocumentCodecProvider()))
   val settings: MongoClientSettings = MongoClientSettings.builder().clusterSettings(clusterSettings).codecRegistry(codecRegistry).build()
   val mongoClient: MongoClient = MongoClient(settings)
   val database: MongoDatabase = mongoClient.getDatabase(dbName);
   
   def collection(collection : String ) : MongoCollection[Document] = {
      database.getCollection(collection)    
   }
   
   def close = mongoClient.close
}

object MongoDB {
   def apply(mongoDbUrls: String, dbName: String): MongoDB = {
      new MongoDB(mongoDbUrls, dbName)
   }
}