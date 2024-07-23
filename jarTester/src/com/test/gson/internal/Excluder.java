/*     */ package com.test.gson.internal;
/*     */ 
/*     */ import com.test.gson.ExclusionStrategy;
/*     */ import com.test.gson.FieldAttributes;
/*     */ import com.test.gson.Gson;
/*     */ import com.test.gson.TypeAdapter;
/*     */ import com.test.gson.TypeAdapterFactory;
/*     */ import com.test.gson.annotations.Expose;
/*     */ import com.test.gson.annotations.Since;
/*     */ import com.test.gson.annotations.Until;
/*     */ import com.test.gson.internal.reflect.ReflectionHelper;
/*     */ import com.test.gson.reflect.TypeToken;
/*     */ import com.test.gson.stream.JsonReader;
/*     */ import com.test.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public final class Excluder
/*     */   implements TypeAdapterFactory, Cloneable
/*     */ {
/*     */   private static final double IGNORE_VERSIONS = -1.0D;
/*  51 */   public static final Excluder DEFAULT = new Excluder();
/*     */   
/*  53 */   private double version = -1.0D;
/*  54 */   private int modifiers = 136;
/*     */   private boolean serializeInnerClasses = true;
/*     */   private boolean requireExpose;
/*  57 */   private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
/*  58 */   private List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();
/*     */ 
/*     */   
/*     */   protected Excluder clone() {
/*     */     try {
/*  63 */       return (Excluder)super.clone();
/*  64 */     } catch (CloneNotSupportedException e) {
/*  65 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Excluder withVersion(double ignoreVersionsAfter) {
/*  70 */     Excluder result = clone();
/*  71 */     result.version = ignoreVersionsAfter;
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   public Excluder withModifiers(int... modifiers) {
/*  76 */     Excluder result = clone();
/*  77 */     result.modifiers = 0;
/*  78 */     for (int modifier : modifiers) {
/*  79 */       result.modifiers |= modifier;
/*     */     }
/*  81 */     return result;
/*     */   }
/*     */   
/*     */   public Excluder disableInnerClassSerialization() {
/*  85 */     Excluder result = clone();
/*  86 */     result.serializeInnerClasses = false;
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   public Excluder excludeFieldsWithoutExposeAnnotation() {
/*  91 */     Excluder result = clone();
/*  92 */     result.requireExpose = true;
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean serialization, boolean deserialization) {
/*  98 */     Excluder result = clone();
/*  99 */     if (serialization) {
/* 100 */       result.serializationStrategies = new ArrayList<>(this.serializationStrategies);
/* 101 */       result.serializationStrategies.add(exclusionStrategy);
/*     */     } 
/* 103 */     if (deserialization) {
/* 104 */       result.deserializationStrategies = new ArrayList<>(this.deserializationStrategies);
/* 105 */       result.deserializationStrategies.add(exclusionStrategy);
/*     */     } 
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
/* 112 */     Class<?> rawType = type.getRawType();
/*     */     
/* 114 */     final boolean skipSerialize = excludeClass(rawType, true);
/* 115 */     final boolean skipDeserialize = excludeClass(rawType, false);
/*     */     
/* 117 */     if (!skipSerialize && !skipDeserialize) {
/* 118 */       return null;
/*     */     }
/*     */     
/* 121 */     return new TypeAdapter<T>()
/*     */       {
/*     */         private volatile TypeAdapter<T> delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public T read(JsonReader in) throws IOException {
/* 130 */           if (skipDeserialize) {
/* 131 */             in.skipValue();
/* 132 */             return null;
/*     */           } 
/* 134 */           return (T)delegate().read(in);
/*     */         }
/*     */ 
/*     */         
/*     */         public void write(JsonWriter out, T value) throws IOException {
/* 139 */           if (skipSerialize) {
/* 140 */             out.nullValue();
/*     */             return;
/*     */           } 
/* 143 */           delegate().write(out, value);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         private TypeAdapter<T> delegate() {
/* 149 */           TypeAdapter<T> d = this.delegate;
/* 150 */           return (d != null) ? d : (this.delegate = gson.getDelegateAdapter(Excluder.this, type));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean excludeField(Field field, boolean serialize) {
/* 156 */     if ((this.modifiers & field.getModifiers()) != 0) {
/* 157 */       return true;
/*     */     }
/*     */     
/* 160 */     if (this.version != -1.0D && 
/* 161 */       !isValidVersion(field.<Since>getAnnotation(Since.class), field.<Until>getAnnotation(Until.class))) {
/* 162 */       return true;
/*     */     }
/*     */     
/* 165 */     if (field.isSynthetic()) {
/* 166 */       return true;
/*     */     }
/*     */     
/* 169 */     if (this.requireExpose) {
/* 170 */       Expose annotation = field.<Expose>getAnnotation(Expose.class);
/* 171 */       if (annotation == null || (serialize ? !annotation.serialize() : !annotation.deserialize())) {
/* 172 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 176 */     if (excludeClass(field.getType(), serialize)) {
/* 177 */       return true;
/*     */     }
/*     */     
/* 180 */     List<ExclusionStrategy> list = serialize ? this.serializationStrategies : this.deserializationStrategies;
/* 181 */     if (!list.isEmpty()) {
/* 182 */       FieldAttributes fieldAttributes = new FieldAttributes(field);
/* 183 */       for (ExclusionStrategy exclusionStrategy : list) {
/* 184 */         if (exclusionStrategy.shouldSkipField(fieldAttributes)) {
/* 185 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean excludeClass(Class<?> clazz, boolean serialize) {
/* 195 */     if (this.version != -1.0D && 
/* 196 */       !isValidVersion(clazz.<Since>getAnnotation(Since.class), clazz.<Until>getAnnotation(Until.class))) {
/* 197 */       return true;
/*     */     }
/*     */     
/* 200 */     if (!this.serializeInnerClasses && isInnerClass(clazz)) {
/* 201 */       return true;
/*     */     }
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
/* 216 */     if (!serialize && 
/* 217 */       !Enum.class.isAssignableFrom(clazz) && 
/* 218 */       ReflectionHelper.isAnonymousOrNonStaticLocal(clazz)) {
/* 219 */       return true;
/*     */     }
/*     */     
/* 222 */     List<ExclusionStrategy> list = serialize ? this.serializationStrategies : this.deserializationStrategies;
/* 223 */     for (ExclusionStrategy exclusionStrategy : list) {
/* 224 */       if (exclusionStrategy.shouldSkipClass(clazz)) {
/* 225 */         return true;
/*     */       }
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isInnerClass(Class<?> clazz) {
/* 232 */     return (clazz.isMemberClass() && !ReflectionHelper.isStatic(clazz));
/*     */   }
/*     */   
/*     */   private boolean isValidVersion(Since since, Until until) {
/* 236 */     return (isValidSince(since) && isValidUntil(until));
/*     */   }
/*     */   
/*     */   private boolean isValidSince(Since annotation) {
/* 240 */     if (annotation != null) {
/* 241 */       double annotationVersion = annotation.value();
/* 242 */       return (this.version >= annotationVersion);
/*     */     } 
/* 244 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isValidUntil(Until annotation) {
/* 248 */     if (annotation != null) {
/* 249 */       double annotationVersion = annotation.value();
/* 250 */       return (this.version < annotationVersion);
/*     */     } 
/* 252 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/Excluder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */