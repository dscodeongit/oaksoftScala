package com.oaksoft.utils.jdbc

import javax.sql.DataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.CallableStatementCallback
import java.sql.CallableStatement
import scala.collection.immutable.Map
import org.apache.logging.log4j.LogManager
import scala.collection.mutable.ListBuffer

class JdbcCallExecutor(ds: DataSource) {
   val logger = LogManager.getLogger(this.getClass.getSimpleName);
   val jdbcTemplate = new JdbcTemplate(ds)

   def execute[T](sql: String, rowMapper : BeanRowMapper[T]): List[T] = {
      //rowMapper.setPrimitivesDefaultedForNullValue(false)
      jdbcTemplate.execute(sql, new CallableStatementCallback[List[T]] {
         override def doInCallableStatement(cs: CallableStatement):List[T] = {
            val retVal = cs.execute();
            val updateCount = cs.getUpdateCount();
            val rs = cs.getResultSet
            var rsList = new ListBuffer[T]
            var row = 1
            
            while(rs.next()) {
               rsList += rowMapper.mapRow(rs, row)
            }
            
            rsList.toList
         }
      })
   }
}