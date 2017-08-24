package com.oaksoft.utils.jdbc

import org.apache.commons.lang3.StringUtils
import org.springframework.jdbc.core.BeanPropertyRowMapper

import java.sql.SQLException
import java.sql.ResultSet
import java.beans.PropertyDescriptor
import org.springframework.jdbc.support.JdbcUtils

class BeanRowMapper[T](mappedClass : Class[T] , checkFullyPopulated : Boolean) extends BeanPropertyRowMapper[T](mappedClass, checkFullyPopulated) {
  
  def this(mappedClass : Class[T] ) {
     this(mappedClass, false)
  }
  
  override def underscoreName(name : String) : String = {
     var un = super.underscoreName(name)
     
     if(un == name && name.endsWith("_")) {
        StringUtils.substringBeforeLast(name, "_")
     }else {
        un
     }
  }   
  
  @throws(classOf[SQLException])
  override def getColumnValue(rs : ResultSet, index : Int, pd: PropertyDescriptor) : Object = {
      val value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType())
      if(value != null && value.isInstanceOf[String]) {
         return value.asInstanceOf[String].trim()
      }
		return value
	}
}

object BeanRowMapper {
   def appy[T](mappedClass : Class[T]) : BeanRowMapper[T] = {
      return new BeanRowMapper(mappedClass)
   }
}