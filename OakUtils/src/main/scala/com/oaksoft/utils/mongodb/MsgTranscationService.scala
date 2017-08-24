package com.oaksoft.utils.mongodb
import org.mongodb.scala.bson.collection.immutable.{Document}
import org.mongodb.scala.model.Filters._
import org.bson.conversions.Bson
   
trait MsgTranscationService {
   def persist (collection: MongoDBCollection, jsonMsg : String, msgId : String) 
   def markProcessed(collection: MongoDBCollection, msgId : String)
   def getUnProcessed(Collection: MongoDBCollection) : List[Document]    
   
}

object MsgTranscationService {   
   val TRANSTATUS_KEY = "transactionStatus"
   def filterOfUncommitted : Bson = {
      or(exists(TRANSTATUS_KEY, false), notEqual(TRANSTATUS_KEY, 1))   
   }
}

object TranscServiceOperation extends Enumeration {
   type TranscServiceOperation = Value
   val PERSIST, MARKPROCESSED, GETUNPROCESSED = Value
}