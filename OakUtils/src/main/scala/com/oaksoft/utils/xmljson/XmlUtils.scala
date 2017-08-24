package com.oaksoft.utils.xmljson

object XmlUtils {
  def prettyXml(xmlStr : String) : String  = {
     val p = new scala.xml.PrettyPrinter(80, 4)
     val xmlNode = scala.xml.XML.loadString(xmlStr)
     p.format(xmlNode)     
  }
}