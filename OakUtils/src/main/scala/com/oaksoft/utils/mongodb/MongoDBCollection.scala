package com.oaksoft.utils.mongodb

import java.util.concurrent.TimeUnit
import org.mongodb.scala.bson.collection.mutable.Document
import com.oaksoft.utils.mongodb.MongoDBCollection._
import com.mongodb.client.model.IndexOptions
import com.oaksoft.utils.mongodb.ObsHelper._
import org.apache.logging.log4j.LogManager
import org.mongodb.scala.MongoCollection
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTimeZone
import org.joda.time.DateTime
import java.util.Date

class MongoDBCollection(name: String, ttlField: String, ttlInSecs: Long, mongoDb: MongoDB) {
   var dbColl: MongoCollection[Document] = _

   try {
      dbColl = mongoDb.collection(name)
      if (ttlInSecs > 0) {
         dbColl.createIndex(Document(ttlField -> 1), new IndexOptions().expireAfter(ttlInSecs, TimeUnit.SECONDS)).results()
      }
   } catch {
      case t: Throwable =>
         logger.error(s"Failed to create expiry index. errMsg= ${t.getMessage}. Caused by: $t")
      //throw new Exception(t)
   }

   def this(name: String, ttlInSecs: Long, mongoDb: MongoDB) {
      this(name, AUDIT_CDATE_KEY, ttlInSecs, mongoDb)
   }

   def this(name: String, mongoDb: MongoDB) {
      this(name, AUDIT_CDATE_KEY, -1, mongoDb)
   }

   def getCollection: MongoCollection[Document] = {
      dbColl
   }
}

object MongoDBCollection {
   val logger = LogManager.getLogger(MongoDBCollection.getClass);
   val dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").withZone(DateTimeZone.UTC);
   val MSG_ID_KEY = "msgId"
   val MSG_PAYLOAD_KEY = "payload"
   val AUDIT_CDATE_KEY = "createDate"
   val AUDIT_MDATE_KEY = "modifiedDate"
   val AUDIT_CBY_KEY = "createBy"
   val AUDIT_MBY_KEY = "modifiedBy"

   def apply(name: String, ttlInSecs: Long, mongoDb: MongoDB): MongoDBCollection = {
      new MongoDBCollection(name, ttlInSecs, mongoDb)
   }

   def apply(name: String, ttlField: String, ttlInSecs: Long, mongoDb: MongoDB): MongoDBCollection = {
      new MongoDBCollection(name, ttlField, ttlInSecs, mongoDb)
   }

   def apply(name: String, mongoDb: MongoDB): MongoDBCollection = {
      new MongoDBCollection(name, mongoDb)
   }

   def buildNewInsertAuditFields(insertBy : String): Document = {
      var dtTime = new Date()
      var doc = Document(AUDIT_CDATE_KEY -> dtTime, AUDIT_MDATE_KEY -> dtTime, AUDIT_CBY_KEY -> insertBy)
      doc
   }
   
   def filterByMsgId (msgId : String) : Document = {
      Document(MSG_ID_KEY -> msgId)
   }

   def buildUpdateAuditFields(updateBy : String): Document = {
      var dtTime = new Date()
      var doc = Document(AUDIT_MDATE_KEY -> dtTime, AUDIT_MBY_KEY -> updateBy)
      doc
   }
}