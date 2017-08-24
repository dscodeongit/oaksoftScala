package com.oaksoft.utils.mongodb

import java.util.Date

import scala.collection.JavaConverters._

import org.apache.logging.log4j.LogManager
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.mongodb.scala.bson.collection.immutable.{ Document => ImmuteDoc }
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.model.Filters._

import com.oaksoft.utils.mongodb.MongoDBCollection._
import com.oaksoft.utils.mongodb.MsgTranscationService.TRANSTATUS_KEY
import com.oaksoft.utils.mongodb.ObsHelper.DocumentObservable
import com.oaksoft.utils.mongodb.ObsHelper.GenericObservable

class MsgTranscationServiceImpl(serviceName: String, useBulk: Boolean) extends MsgTranscationService {
   val logger = LogManager.getLogger(MsgTranscationServiceImpl.getClass);

   val dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").withZone(DateTimeZone.UTC);

   def this(serviceName: String) {
      this(serviceName, false)
   }
   
   def find(collection: MongoDBCollection, date: Date) : java.util.List[String] = {
      collection.getCollection.find(gte("createDate", date)).results().map(_.toJson()).asJava
   }
   //@throws(classOf[Exception])
   def persist(collection: MongoDBCollection, jsonMsg: String, msgId: String) = {
      try {
         var doc = Document(MSG_ID_KEY -> msgId, MSG_PAYLOAD_KEY -> Document(jsonMsg))
         doc = doc ++ buildNewInsertAuditFields(serviceName)        
         collection.getCollection.insertOne(doc).results()
         logger.info(s"Inserted message with msgId = $msgId")
      } catch {
         case t: Throwable =>
            logger.error(s"Failed to insert message with msgId = $msgId. errMsg= ${t.getMessage}. Caused by: $t")
         //throw new Exception(t)
      }
   }

   //@throws(classOf[Exception])
   def markProcessed(collection: MongoDBCollection, msgId: String) = {
      try {
         var updoc = buildUpdateAuditFields(serviceName) + (TRANSTATUS_KEY -> 1)
         var setDoc = Document("$set" -> updoc)
         collection.getCollection.updateOne(equal(MSG_ID_KEY, msgId), setDoc).results()
         logger.info(s"Updated status for message with msgId = $msgId")
      } catch {
         case t: Throwable =>
            logger.error(s"Failed to update status for message with msgId = $msgId. errMsg= ${t.getMessage}. Caused by: $t")
         // throw new Exception(t)
      }
   }

   //@throws(classOf[Exception])
   def getUnProcessed(collection: MongoDBCollection): List[ImmuteDoc] = {
      try {
         var docs = collection.getCollection.find(equal(TRANSTATUS_KEY, 0)).results()
         logger.info(s"Retrieved un-processed messages.")
         docs.toList
      } catch {
         case t: Throwable =>
            logger.error(s"Failed to retrieve un-processed messages. errMsg= ${t.getMessage}. Caused by: $t")
         //throw new Exception(t)
      }
      List.empty
   }

}

object MsgTranscationServiceImpl {
   def apply(serviceName: String): MsgTranscationServiceImpl = {
      new MsgTranscationServiceImpl(serviceName)
   }
}