/*     */ package com.test.gson.internal.reflect;
/*     */ 
/*     */ import com.test.gson.JsonIOException;
/*     */ import com.test.gson.internal.TroubleshootingGuide;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class ReflectionHelper
/*     */ {
/*     */   private static final RecordHelper RECORD_HELPER;
/*     */   
/*     */   static {
/*     */     RecordHelper instance;
/*     */     try {
/*  37 */       instance = new RecordSupportedHelper();
/*  38 */     } catch (ReflectiveOperationException e) {
/*  39 */       instance = new RecordNotSupportedHelper();
/*     */     } 
/*  41 */     RECORD_HELPER = instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getInaccessibleTroubleshootingSuffix(Exception e) {
/*  48 */     if (e.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
/*  49 */       String message = e.getMessage();
/*     */ 
/*     */ 
/*     */       
/*  53 */       String troubleshootingId = (message != null && message.contains("to module com.google.gson")) ? "reflection-inaccessible-to-module-gson" : "reflection-inaccessible";
/*  54 */       return "\nSee " + TroubleshootingGuide.createUrl(troubleshootingId);
/*     */     } 
/*  56 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeAccessible(AccessibleObject object) throws JsonIOException {
/*     */     try {
/*  68 */       object.setAccessible(true);
/*  69 */     } catch (Exception exception) {
/*  70 */       String description = getAccessibleObjectDescription(object, false);
/*  71 */       throw new JsonIOException("Failed making " + description + " accessible; either increase its visibility or write a custom TypeAdapter for its declaring type." + 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  76 */           getInaccessibleTroubleshootingSuffix(exception), exception);
/*     */     } 
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
/*     */   public static String getAccessibleObjectDescription(AccessibleObject object, boolean uppercaseFirstLetter) {
/*     */     String description;
/*  93 */     if (object instanceof Field) {
/*  94 */       description = "field '" + fieldToString((Field)object) + "'";
/*  95 */     } else if (object instanceof Method) {
/*  96 */       Method method = (Method)object;
/*     */       
/*  98 */       StringBuilder methodSignatureBuilder = new StringBuilder(method.getName());
/*  99 */       appendExecutableParameters(method, methodSignatureBuilder);
/* 100 */       String methodSignature = methodSignatureBuilder.toString();
/*     */       
/* 102 */       description = "method '" + method.getDeclaringClass().getName() + "#" + methodSignature + "'";
/* 103 */     } else if (object instanceof Constructor) {
/* 104 */       description = "constructor '" + constructorToString((Constructor)object) + "'";
/*     */     } else {
/* 106 */       description = "<unknown AccessibleObject> " + object.toString();
/*     */     } 
/*     */     
/* 109 */     if (uppercaseFirstLetter && Character.isLowerCase(description.charAt(0))) {
/* 110 */       description = Character.toUpperCase(description.charAt(0)) + description.substring(1);
/*     */     }
/* 112 */     return description;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String fieldToString(Field field) {
/* 117 */     return field.getDeclaringClass().getName() + "#" + field.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String constructorToString(Constructor<?> constructor) {
/* 125 */     StringBuilder stringBuilder = new StringBuilder(constructor.getDeclaringClass().getName());
/* 126 */     appendExecutableParameters(constructor, stringBuilder);
/*     */     
/* 128 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendExecutableParameters(AccessibleObject executable, StringBuilder stringBuilder) {
/* 134 */     stringBuilder.append('(');
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     Class<?>[] parameters = (executable instanceof Method) ? ((Method)executable).getParameterTypes() : ((Constructor)executable).getParameterTypes();
/* 140 */     for (int i = 0; i < parameters.length; i++) {
/* 141 */       if (i > 0) {
/* 142 */         stringBuilder.append(", ");
/*     */       }
/* 144 */       stringBuilder.append(parameters[i].getSimpleName());
/*     */     } 
/*     */     
/* 147 */     stringBuilder.append(')');
/*     */   }
/*     */   
/*     */   public static boolean isStatic(Class<?> clazz) {
/* 151 */     return Modifier.isStatic(clazz.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAnonymousOrNonStaticLocal(Class<?> clazz) {
/* 156 */     return (!isStatic(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String tryMakeAccessible(Constructor<?> constructor) {
/*     */     try {
/* 167 */       constructor.setAccessible(true);
/* 168 */       return null;
/* 169 */     } catch (Exception exception) {
/* 170 */       return "Failed making constructor '" + 
/* 171 */         constructorToString(constructor) + "' accessible; either increase its visibility or write a custom InstanceCreator or TypeAdapter for its declaring type: " + exception
/*     */ 
/*     */ 
/*     */         
/* 175 */         .getMessage() + 
/* 176 */         getInaccessibleTroubleshootingSuffix(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isRecord(Class<?> raw) {
/* 182 */     return RECORD_HELPER.isRecord(raw);
/*     */   }
/*     */   
/*     */   public static String[] getRecordComponentNames(Class<?> raw) {
/* 186 */     return RECORD_HELPER.getRecordComponentNames(raw);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method getAccessor(Class<?> raw, Field field) {
/* 191 */     return RECORD_HELPER.getAccessor(raw, field);
/*     */   }
/*     */   
/*     */   public static <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
/* 195 */     return RECORD_HELPER.getCanonicalRecordConstructor(raw);
/*     */   }
/*     */ 
/*     */   
/*     */   public static RuntimeException createExceptionForUnexpectedIllegalAccess(IllegalAccessException exception) {
/* 200 */     throw new RuntimeException("Unexpected IllegalAccessException occurred (Gson 2.11.0). Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using ReflectionAccessFilter, report this to the Gson maintainers.", exception);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RuntimeException createExceptionForRecordReflectionException(ReflectiveOperationException exception) {
/* 210 */     throw new RuntimeException("Unexpected ReflectiveOperationException occurred (Gson 2.11.0). To support Java records, reflection is utilized to read out information about records. All these invocations happens after it is established that records exist in the JVM. This exception is unexpected behavior.", exception);
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class RecordHelper
/*     */   {
/*     */     private RecordHelper() {}
/*     */ 
/*     */     
/*     */     abstract boolean isRecord(Class<?> param1Class);
/*     */ 
/*     */     
/*     */     abstract String[] getRecordComponentNames(Class<?> param1Class);
/*     */ 
/*     */     
/*     */     abstract <T> Constructor<T> getCanonicalRecordConstructor(Class<T> param1Class);
/*     */     
/*     */     public abstract Method getAccessor(Class<?> param1Class, Field param1Field);
/*     */   }
/*     */   
/*     */   private static class RecordSupportedHelper
/*     */     extends RecordHelper
/*     */   {
/*     */     private final Method isRecord;
/*     */     private final Method getRecordComponents;
/*     */     private final Method getName;
/*     */     private final Method getType;
/*     */     
/*     */     private RecordSupportedHelper() throws NoSuchMethodException, ClassNotFoundException {
/* 239 */       this.isRecord = Class.class.getMethod("isRecord", new Class[0]);
/* 240 */       this.getRecordComponents = Class.class.getMethod("getRecordComponents", new Class[0]);
/* 241 */       Class<?> classRecordComponent = Class.forName("java.lang.reflect.RecordComponent");
/* 242 */       this.getName = classRecordComponent.getMethod("getName", new Class[0]);
/* 243 */       this.getType = classRecordComponent.getMethod("getType", new Class[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isRecord(Class<?> raw) {
/*     */       try {
/* 249 */         return ((Boolean)this.isRecord.invoke(raw, new Object[0])).booleanValue();
/* 250 */       } catch (ReflectiveOperationException e) {
/* 251 */         throw ReflectionHelper.createExceptionForRecordReflectionException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     String[] getRecordComponentNames(Class<?> raw) {
/*     */       try {
/* 258 */         Object[] recordComponents = (Object[])this.getRecordComponents.invoke(raw, new Object[0]);
/* 259 */         String[] componentNames = new String[recordComponents.length];
/* 260 */         for (int i = 0; i < recordComponents.length; i++) {
/* 261 */           componentNames[i] = (String)this.getName.invoke(recordComponents[i], new Object[0]);
/*     */         }
/* 263 */         return componentNames;
/* 264 */       } catch (ReflectiveOperationException e) {
/* 265 */         throw ReflectionHelper.createExceptionForRecordReflectionException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
/*     */       try {
/* 272 */         Object[] recordComponents = (Object[])this.getRecordComponents.invoke(raw, new Object[0]);
/* 273 */         Class<?>[] recordComponentTypes = new Class[recordComponents.length];
/* 274 */         for (int i = 0; i < recordComponents.length; i++) {
/* 275 */           recordComponentTypes[i] = (Class)this.getType.invoke(recordComponents[i], new Object[0]);
/*     */         }
/*     */ 
/*     */         
/* 279 */         return raw.getDeclaredConstructor(recordComponentTypes);
/* 280 */       } catch (ReflectiveOperationException e) {
/* 281 */         throw ReflectionHelper.createExceptionForRecordReflectionException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Method getAccessor(Class<?> raw, Field field) {
/*     */       try {
/* 291 */         return raw.getMethod(field.getName(), new Class[0]);
/* 292 */       } catch (ReflectiveOperationException e) {
/* 293 */         throw ReflectionHelper.createExceptionForRecordReflectionException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RecordNotSupportedHelper
/*     */     extends RecordHelper {
/*     */     private RecordNotSupportedHelper() {}
/*     */     
/*     */     boolean isRecord(Class<?> clazz) {
/* 303 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     String[] getRecordComponentNames(Class<?> clazz) {
/* 308 */       throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
/* 314 */       throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Method getAccessor(Class<?> raw, Field field) {
/* 320 */       throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/reflect/ReflectionHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */