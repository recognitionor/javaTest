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
/*    */ 
/*    */ public final class JsonSyntaxException
/*    */   extends JsonParseException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JsonSyntaxException(String msg) {
/* 29 */     super(msg);
/*    */   }
/*    */   
/*    */   public JsonSyntaxException(String msg, Throwable cause) {
/* 33 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonSyntaxException(Throwable cause) {
/* 43 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonSyntaxException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */