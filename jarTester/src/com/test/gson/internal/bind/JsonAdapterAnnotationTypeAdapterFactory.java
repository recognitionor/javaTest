/*     */ package com.test.gson.internal.bind;
/*     */ 
/*     */ import com.test.gson.Gson;
/*     */ import com.test.gson.JsonDeserializer;
/*     */ import com.test.gson.JsonSerializer;
/*     */ import com.test.gson.TypeAdapter;
/*     */ import com.test.gson.TypeAdapterFactory;
/*     */ import com.test.gson.annotations.JsonAdapter;
/*     */ import com.test.gson.internal.ConstructorConstructor;
/*     */ import com.test.gson.reflect.TypeToken;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public final class JsonAdapterAnnotationTypeAdapterFactory
/*     */   implements TypeAdapterFactory
/*     */ {
/*     */   private static class DummyTypeAdapterFactory
/*     */     implements TypeAdapterFactory
/*     */   {
/*     */     private DummyTypeAdapterFactory() {}
/*     */     
/*     */     public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/*  41 */       throw new AssertionError("Factory should not be used");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  46 */   private static final TypeAdapterFactory TREE_TYPE_CLASS_DUMMY_FACTORY = new DummyTypeAdapterFactory();
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final TypeAdapterFactory TREE_TYPE_FIELD_DUMMY_FACTORY = new DummyTypeAdapterFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConstructorConstructor constructorConstructor;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConcurrentMap<Class<?>, TypeAdapterFactory> adapterFactoryMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
/*  65 */     this.constructorConstructor = constructorConstructor;
/*  66 */     this.adapterFactoryMap = new ConcurrentHashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   private static JsonAdapter getAnnotation(Class<?> rawType) {
/*  71 */     return rawType.<JsonAdapter>getAnnotation(JsonAdapter.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
/*  78 */     Class<? super T> rawType = targetType.getRawType();
/*  79 */     JsonAdapter annotation = getAnnotation(rawType);
/*  80 */     if (annotation == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     return (TypeAdapter)
/*  84 */       getTypeAdapter(this.constructorConstructor, gson, targetType, annotation, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object createAdapter(ConstructorConstructor constructorConstructor, Class<?> adapterClass) {
/*  93 */     return constructorConstructor.get(TypeToken.get(adapterClass)).construct();
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeAdapterFactory putFactoryAndGetCurrent(Class<?> rawType, TypeAdapterFactory factory) {
/*  98 */     TypeAdapterFactory existingFactory = this.adapterFactoryMap.putIfAbsent(rawType, factory);
/*  99 */     return (existingFactory != null) ? existingFactory : factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> type, JsonAdapter annotation, boolean isClassAnnotation) {
/*     */     TypeAdapter<?> typeAdapter;
/* 108 */     Object instance = createAdapter(constructorConstructor, annotation.value());
/*     */ 
/*     */     
/* 111 */     boolean nullSafe = annotation.nullSafe();
/* 112 */     if (instance instanceof TypeAdapter) {
/* 113 */       typeAdapter = (TypeAdapter)instance;
/* 114 */     } else if (instance instanceof TypeAdapterFactory) {
/* 115 */       TypeAdapterFactory factory = (TypeAdapterFactory)instance;
/*     */       
/* 117 */       if (isClassAnnotation) {
/* 118 */         factory = putFactoryAndGetCurrent(type.getRawType(), factory);
/*     */       }
/*     */       
/* 121 */       typeAdapter = factory.create(gson, type);
/* 122 */     } else if (instance instanceof JsonSerializer || instance instanceof JsonDeserializer) {
/*     */       TypeAdapterFactory skipPast;
/* 124 */       JsonSerializer<?> serializer = (instance instanceof JsonSerializer) ? (JsonSerializer)instance : null;
/*     */       
/* 126 */       JsonDeserializer<?> deserializer = (instance instanceof JsonDeserializer) ? (JsonDeserializer)instance : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       if (isClassAnnotation) {
/* 133 */         skipPast = TREE_TYPE_CLASS_DUMMY_FACTORY;
/*     */       } else {
/* 135 */         skipPast = TREE_TYPE_FIELD_DUMMY_FACTORY;
/*     */       } 
/*     */       
/* 138 */       TypeAdapter<?> tempAdapter = new TreeTypeAdapter(serializer, deserializer, gson, type, skipPast, nullSafe);
/*     */       
/* 140 */       typeAdapter = tempAdapter;
/*     */ 
/*     */       
/* 143 */       nullSafe = false;
/*     */     } else {
/* 145 */       throw new IllegalArgumentException("Invalid attempt to bind an instance of " + instance
/*     */           
/* 147 */           .getClass().getName() + " as a @JsonAdapter for " + type
/*     */           
/* 149 */           .toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (typeAdapter != null && nullSafe) {
/* 155 */       typeAdapter = typeAdapter.nullSafe();
/*     */     }
/*     */     
/* 158 */     return typeAdapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassJsonAdapterFactory(TypeToken<?> type, TypeAdapterFactory factory) {
/* 166 */     Objects.requireNonNull(type);
/* 167 */     Objects.requireNonNull(factory);
/*     */     
/* 169 */     if (factory == TREE_TYPE_CLASS_DUMMY_FACTORY) {
/* 170 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 174 */     Class<?> rawType = type.getRawType();
/*     */     
/* 176 */     TypeAdapterFactory existingFactory = this.adapterFactoryMap.get(rawType);
/* 177 */     if (existingFactory != null)
/*     */     {
/* 179 */       return (existingFactory == factory);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     JsonAdapter annotation = getAnnotation(rawType);
/* 187 */     if (annotation == null) {
/* 188 */       return false;
/*     */     }
/*     */     
/* 191 */     Class<?> adapterClass = annotation.value();
/* 192 */     if (!TypeAdapterFactory.class.isAssignableFrom(adapterClass)) {
/* 193 */       return false;
/*     */     }
/*     */     
/* 196 */     Object adapter = createAdapter(this.constructorConstructor, adapterClass);
/* 197 */     TypeAdapterFactory newFactory = (TypeAdapterFactory)adapter;
/*     */     
/* 199 */     return (putFactoryAndGetCurrent(rawType, newFactory) == factory);
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/bind/JsonAdapterAnnotationTypeAdapterFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */