package com.oaksoft.utils.mail

import javax.mail._
import javax.mail.internet._
import java.util.Date
import java.util.Properties
import scala.collection.JavaConversions._
import org.apache.commons.lang3.StringUtils
import javax.mail.MessagingException
import javax.mail.internet.AddressException

class SimpleMailAgent(to: String,
                cc: String,
                bcc: String,
                from: String,
                smtpHost: String,
                subPrefix : String) {

   // throws MessagingException
   def sendMessage(subject: String, content: String) {
      var message = createMessage
      message.setFrom(new InternetAddress(from))
      setToCcBccRecipients(message)
      message.setSentDate(new Date())
      var sub = subject;
      if(StringUtils.isNotBlank(subPrefix)){
         sub = subPrefix + subject 
      }
      message.setSubject(sub)
      message.setText(content)
      Transport.send(message)
   }

   def createMessage: Message = {
      val properties = new Properties()
      properties.put("mail.smtp.host", smtpHost)
      val session = Session.getDefaultInstance(properties, null)
      return new MimeMessage(session)
   }

   // throws AddressException, MessagingException
  @throws(classOf[AddressException])
  @throws(classOf[MessagingException])
   def setToCcBccRecipients (message: Message) {
      setMessageRecipients(message, to, Message.RecipientType.TO)
      if (cc != null) {
         setMessageRecipients(message, cc, Message.RecipientType.CC)
      }
      if (bcc != null) {
         setMessageRecipients(message, bcc, Message.RecipientType.BCC)
      }
   }

   // throws AddressException, MessagingException
   @throws(classOf[AddressException])
   @throws(classOf[MessagingException])
   def setMessageRecipients(message: Message, recipient: String, recipientType: Message.RecipientType) {
      // had to do the asInstanceOf[...] call here to make scala happy
      val addressArray = buildInternetAddressArray(recipient).asInstanceOf[Array[Address]]
      if ((addressArray != null) && (addressArray.length > 0)) {
         message.setRecipients(recipientType, addressArray)
      }
   }

   // throws AddressException
   @throws(classOf[AddressException])
   def buildInternetAddressArray(address: String): Array[InternetAddress] = {
      // could test for a null or blank String but I'm letting parse just throw an exception
      return InternetAddress.parse(address)
   }

}
