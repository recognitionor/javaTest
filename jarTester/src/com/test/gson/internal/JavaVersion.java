/*    */ package com.test.gson.internal;
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
/*    */ 
/*    */ public final class JavaVersion
/*    */ {
/* 26 */   private static final int majorJavaVersion = determineMajorJavaVersion();
/*    */   
/*    */   private static int determineMajorJavaVersion() {
/* 29 */     String javaVersion = System.getProperty("java.version");
/* 30 */     return parseMajorJavaVersion(javaVersion);
/*    */   }
/*    */ 
/*    */   
/*    */   static int parseMajorJavaVersion(String javaVersion) {
/* 35 */     int version = parseDotted(javaVersion);
/* 36 */     if (version == -1) {
/* 37 */       version = extractBeginningInt(javaVersion);
/*    */     }
/* 39 */     if (version == -1) {
/* 40 */       return 6;
/*    */     }
/* 42 */     return version;
/*    */   }
/*    */ 
/*    */   
/*    */   private static int parseDotted(String javaVersion) {
/*    */     try {
/* 48 */       String[] parts = javaVersion.split("[._]", 3);
/* 49 */       int firstVer = Integer.parseInt(parts[0]);
/* 50 */       if (firstVer == 1 && parts.length > 1) {
/* 51 */         return Integer.parseInt(parts[1]);
/*    */       }
/* 53 */       return firstVer;
/*    */     }
/* 55 */     catch (NumberFormatException e) {
/* 56 */       return -1;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int extractBeginningInt(String javaVersion) {
/*    */     try {
/* 62 */       StringBuilder num = new StringBuilder();
/* 63 */       for (int i = 0; i < javaVersion.length(); ) {
/* 64 */         char c = javaVersion.charAt(i);
/* 65 */         if (Character.isDigit(c)) {
/* 66 */           num.append(c);
/*    */           
/*    */           i++;
/*    */         } 
/*    */       } 
/* 71 */       return Integer.parseInt(num.toString());
/* 72 */     } catch (NumberFormatException e) {
/* 73 */       return -1;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getMajorJavaVersion() {
/* 83 */     return majorJavaVersion;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isJava9OrLater() {
/* 93 */     return (majorJavaVersion >= 9);
/*    */   }
/*    */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/JavaVersion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */