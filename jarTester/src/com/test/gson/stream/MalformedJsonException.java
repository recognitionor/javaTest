/*    */ package com.test.gson.stream;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public final class MalformedJsonException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MalformedJsonException(String msg) {
/* 30 */     super(msg);
/*    */   }
/*    */   
/*    */   public MalformedJsonException(String msg, Throwable throwable) {
/* 34 */     super(msg, throwable);
/*    */   }
/*    */   
/*    */   public MalformedJsonException(Throwable throwable) {
/* 38 */     super(throwable);
/*    */   }
/*    */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/stream/MalformedJsonException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */