/*    */ package com.test.gson;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JsonIOException
/*    */   extends JsonParseException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JsonIOException(String msg) {
/* 28 */     super(msg);
/*    */   }
/*    */   
/*    */   public JsonIOException(String msg, Throwable cause) {
/* 32 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonIOException(Throwable cause) {
/* 42 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonIOException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */