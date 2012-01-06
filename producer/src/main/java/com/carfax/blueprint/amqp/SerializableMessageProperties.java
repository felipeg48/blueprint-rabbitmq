package com.carfax.blueprint.amqp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;

public class SerializableMessageProperties implements Serializable{
   
   public static final String CONTENT_TYPE_BYTES = "application/octet-stream";

   public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

   public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";

   public static final String CONTENT_TYPE_JSON = "application/json";

   private static final String DEFAULT_CONTENT_TYPE = CONTENT_TYPE_BYTES;

   private static final MessageDeliveryMode DEFAULT_DELIVERY_MODE = MessageDeliveryMode.PERSISTENT;

   private static final Integer DEFAULT_PRIORITY = new Integer(0);

   private Map<String, Object> headers = new HashMap<String, Object>();

   private  Date timestamp;

   private  String messageId;

   private  String userId;

   private  String appId;

   private  String clusterId;

   private  String type;

   private  byte[] correlationId;

   private  String replyTo;

   private  String contentType = DEFAULT_CONTENT_TYPE;

   private  String contentEncoding;

   private  long contentLength;

   private  MessageDeliveryMode deliveryMode = DEFAULT_DELIVERY_MODE;

   private  String expiration;

   private  Integer priority = DEFAULT_PRIORITY;

   private  Boolean redelivered;

   private  String receivedExchange;

   private  String receivedRoutingKey;

   private  long deliveryTag;

   private  Integer messageCount;


   public Map<String, Object> getHeaders() {
      return this.headers;
   }

   public void setTimestamp(Date timestamp) {
      this.timestamp = timestamp;
   }
   public void setHeaders(Map<String, Object> headers) {
      this.headers = headers;
   }

   // NOTE qpid java timestamp is long, presumably can convert to Date.
   public Date getTimestamp() {
      return this.timestamp;
   }

   // NOTE Not forward compatible with qpid 1.0 .NET
   // qpid 0.8 .NET/Java: is a string
   // qpid 1.0 .NET: MessageId property on class MessageProperties and is UUID
   // There is an 'ID' stored IMessage class and is an int.
   public void setMessageId(String messageId) {
      this.messageId = messageId;
   }

   public String getMessageId() {
      return this.messageId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   // NOTE Note forward compatible with qpid 1.0 .NET
   // qpid 0.8 .NET/java: is a string
   // qpid 1.0 .NET: getUserId is byte[]
   public String getUserId() {
      return this.userId;
   }

   public void setAppId(String appId) {
      this.appId = appId;
   }

   public String getAppId() {
      return this.appId;
   }

   // NOTE not forward compatible with qpid 1.0 .NET
   // qpid 0.8 .NET/Java: is a string
   // qpid 1.0 .NET: is not present
   public void setClusterId(String clusterId) {
      this.clusterId = clusterId;
      ;
   }

   public String getClusterId() {
      return this.clusterId;
   }

   public void setType(String type) {
      this.type = type;
   }

   // NOTE stuctureType is int in qpid
   public String getType() {
      return this.type;
   }

   public void setCorrelationId(byte[] correlationId) {
      this.correlationId = correlationId;
   }

   public byte[] getCorrelationId() {
      return this.correlationId;
   }

   public void setReplyTo(String replyTo) {
      this.replyTo = replyTo;
   }

   public String getReplyTo() {
      return this.replyTo;
   }

   public void setReplyToAddress(Address replyTo) {
      this.replyTo = (replyTo != null) ? replyTo.toString() : null;
   }

   public Address getReplyToAddress() {
      return (this.replyTo != null) ? new Address(this.replyTo) : null;
   }

   public void setContentType(String contentType) {
      this.contentType = contentType;
   }

   public String getContentType() {
      return this.contentType;
   }

   public void setContentEncoding(String contentEncoding) {
      this.contentEncoding = contentEncoding;
   }

   public String getContentEncoding() {
      return this.contentEncoding;
   }

   public void setContentLength(long contentLength) {
      this.contentLength = contentLength;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public void setDeliveryMode(MessageDeliveryMode deliveryMode) {
      this.deliveryMode = deliveryMode;
   }

   public MessageDeliveryMode getDeliveryMode() {
      return this.deliveryMode;
   }

   // why not a Date or long?
   public void setExpiration(String expiration) {
      this.expiration = expiration;
   }

   // NOTE qpid Java broker qpid 0.8/1.0 .NET: is a long.
   // 0.8 Spec has: expiration (shortstr)
   public String getExpiration() {
      return this.expiration;
   }

   public void setPriority(Integer priority) {
      this.priority = priority;
   }

   public Integer getPriority() {
      return this.priority;
   }

   public void setReceivedExchange(String receivedExchange) {
      this.receivedExchange = receivedExchange;
   }

   public String getReceivedExchange() {
      return this.receivedExchange;
   }

   public void setReceivedRoutingKey(String receivedRoutingKey) {
      this.receivedRoutingKey = receivedRoutingKey;
   }

   public String getReceivedRoutingKey() {
      return this.receivedRoutingKey;
   }

   public void setRedelivered(Boolean redelivered) {
      this.redelivered = redelivered;
   }

   public Boolean isRedelivered() {
      return this.redelivered;
   }

   public void setDeliveryTag(long deliveryTag) {
      this.deliveryTag = deliveryTag;
   }

   public long getDeliveryTag() {
      return this.deliveryTag;
   }

   public void setMessageCount(Integer messageCount) {
      this.messageCount = messageCount;
   }

   public Integer getMessageCount() {
      return this.messageCount;
   }

   public static SerializableMessageProperties from(MessageProperties messsageProps){
      SerializableMessageProperties props = new SerializableMessageProperties();
      BeanUtils.copyProperties(messsageProps, props);
      props.setHeaders(messsageProps.getHeaders());
      return props;
   }
   public MessageProperties toMessageProperties(){
      MessageProperties props = new MessageProperties();
      BeanUtils.copyProperties(this, props);
      for(Entry<String, Object> entry : this.headers.entrySet()){
         props.setHeader(entry.getKey(), entry.getValue());
      }
      return props;
   }
}
