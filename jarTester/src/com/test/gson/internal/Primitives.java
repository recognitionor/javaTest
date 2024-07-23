/*    */ package com.test.gson.internal;
/*    */ 
/*    */ import java.lang.reflect.Type;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Primitives
/*    */ {
/*    */   public static boolean isPrimitive(Type type) {
/* 32 */     return (type instanceof Class && ((Class)type).isPrimitive());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isWrapperType(Type type) {
/* 42 */     return (type == Integer.class || type == Float.class || type == Byte.class || type == Double.class || type == Long.class || type == Character.class || type == Boolean.class || type == Short.class || type == Void.class);
/*    */   }
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
/*    */   public static <T> Class<T> wrap(Class<T> type) {
/* 65 */     if (type == int.class) return (Class)Integer.class; 
/* 66 */     if (type == float.class) return (Class)Float.class; 
/* 67 */     if (type == byte.class) return (Class)Byte.class; 
/* 68 */     if (type == double.class) return (Class)Double.class; 
/* 69 */     if (type == long.class) return (Class)Long.class; 
/* 70 */     if (type == char.class) return (Class)Character.class; 
/* 71 */     if (type == boolean.class) return (Class)Boolean.class; 
/* 72 */     if (type == short.class) return (Class)Short.class; 
/* 73 */     if (type == void.class) return (Class)Void.class; 
/* 74 */     return type;
/*    */   }
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
/*    */   public static <T> Class<T> unwrap(Class<T> type) {
/* 89 */     if (type == Integer.class) return (Class)int.class; 
/* 90 */     if (type == Float.class) return (Class)float.class; 
/* 91 */     if (type == Byte.class) return (Class)byte.class; 
/* 92 */     if (type == Double.class) return (Class)double.class; 
/* 93 */     if (type == Long.class) return (Class)long.class; 
/* 94 */     if (type == Character.class) return (Class)char.class; 
/* 95 */     if (type == Boolean.class) return (Class)boolean.class; 
/* 96 */     if (type == Short.class) return (Class)short.class; 
/* 97 */     if (type == Void.class) return (Class)void.class; 
/* 98 */     return type;
/*    */   }
/*    */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/Primitives.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */