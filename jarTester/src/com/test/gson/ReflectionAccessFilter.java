/*     */ package com.test.gson;
/*     */ 
/*     */ import com.test.gson.internal.ReflectionAccessFilterHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ReflectionAccessFilter
/*     */ {
/*     */   public enum FilterResult
/*     */   {
/*  58 */     ALLOW,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     INDECISIVE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     BLOCK_INACCESSIBLE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     BLOCK_ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static final ReflectionAccessFilter BLOCK_INACCESSIBLE_JAVA = new ReflectionAccessFilter()
/*     */     {
/*     */       public FilterResult check(Class<?> rawClass)
/*     */       {
/* 116 */         return ReflectionAccessFilterHelper.isJavaType(rawClass) ? 
/* 117 */           FilterResult.BLOCK_INACCESSIBLE :
/* 118 */           FilterResult.INDECISIVE;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 123 */         return "ReflectionAccessFilter#BLOCK_INACCESSIBLE_JAVA";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static final ReflectionAccessFilter BLOCK_ALL_JAVA = new ReflectionAccessFilter()
/*     */     {
/*     */       public FilterResult check(Class<?> rawClass)
/*     */       {
/* 147 */         return ReflectionAccessFilterHelper.isJavaType(rawClass) ? 
/* 148 */           FilterResult.BLOCK_ALL :
/* 149 */           FilterResult.INDECISIVE;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 154 */         return "ReflectionAccessFilter#BLOCK_ALL_JAVA";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final ReflectionAccessFilter BLOCK_ALL_ANDROID = new ReflectionAccessFilter()
/*     */     {
/*     */       public FilterResult check(Class<?> rawClass)
/*     */       {
/* 178 */         return ReflectionAccessFilterHelper.isAndroidType(rawClass) ? 
/* 179 */           FilterResult.BLOCK_ALL :
/* 180 */           FilterResult.INDECISIVE;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 185 */         return "ReflectionAccessFilter#BLOCK_ALL_ANDROID";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public static final ReflectionAccessFilter BLOCK_ALL_PLATFORM = new ReflectionAccessFilter()
/*     */     {
/*     */       public FilterResult check(Class<?> rawClass)
/*     */       {
/* 210 */         return ReflectionAccessFilterHelper.isAnyPlatformType(rawClass) ? 
/* 211 */           FilterResult.BLOCK_ALL :
/* 212 */           FilterResult.INDECISIVE;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 217 */         return "ReflectionAccessFilter#BLOCK_ALL_PLATFORM";
/*     */       }
/*     */     };
/*     */   
/*     */   FilterResult check(Class<?> paramClass);
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/ReflectionAccessFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */