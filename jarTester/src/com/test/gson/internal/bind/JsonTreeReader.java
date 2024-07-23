/*     */ package com.test.gson.internal.bind;
/*     */ 
/*     */ import com.test.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.test.gson.JsonArray;
/*     */ import com.test.gson.JsonElement;
/*     */ import com.test.gson.JsonObject;
/*     */ import com.test.gson.JsonPrimitive;
/*     */ import com.test.gson.stream.JsonReader;
/*     */ import com.test.gson.stream.JsonToken;
/*     */ import com.test.gson.stream.MalformedJsonException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public final class JsonTreeReader
/*     */   extends JsonReader
/*     */ {
/*  40 */   private static final Reader UNREADABLE_READER = new Reader()
/*     */     {
/*     */       public int read(char[] buffer, int offset, int count)
/*     */       {
/*  44 */         throw new AssertionError();
/*     */       }
/*     */ 
/*     */       
/*     */       public void close() {
/*  49 */         throw new AssertionError();
/*     */       }
/*     */     };
/*  52 */   private static final Object SENTINEL_CLOSED = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private Object[] stack = new Object[32];
/*  58 */   private int stackSize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private String[] pathNames = new String[32];
/*  69 */   private int[] pathIndices = new int[32];
/*     */   
/*     */   public JsonTreeReader(JsonElement element) {
/*  72 */     super(UNREADABLE_READER);
/*  73 */     push(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginArray() throws IOException {
/*  78 */     expect(JsonToken.BEGIN_ARRAY);
/*  79 */     JsonArray array = (JsonArray)peekStack();
/*  80 */     push(array.iterator());
/*  81 */     this.pathIndices[this.stackSize - 1] = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endArray() throws IOException {
/*  86 */     expect(JsonToken.END_ARRAY);
/*  87 */     popStack();
/*  88 */     popStack();
/*  89 */     if (this.stackSize > 0) {
/*  90 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginObject() throws IOException {
/*  96 */     expect(JsonToken.BEGIN_OBJECT);
/*  97 */     JsonObject object = (JsonObject)peekStack();
/*  98 */     push(object.entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public void endObject() throws IOException {
/* 103 */     expect(JsonToken.END_OBJECT);
/* 104 */     this.pathNames[this.stackSize - 1] = null;
/* 105 */     popStack();
/* 106 */     popStack();
/* 107 */     if (this.stackSize > 0) {
/* 108 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() throws IOException {
/* 114 */     JsonToken token = peek();
/* 115 */     return (token != JsonToken.END_OBJECT && token != JsonToken.END_ARRAY && token != JsonToken.END_DOCUMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonToken peek() throws IOException {
/* 122 */     if (this.stackSize == 0) {
/* 123 */       return JsonToken.END_DOCUMENT;
/*     */     }
/*     */     
/* 126 */     Object o = peekStack();
/* 127 */     if (o instanceof Iterator) {
/* 128 */       boolean isObject = this.stack[this.stackSize - 2] instanceof JsonObject;
/* 129 */       Iterator<?> iterator = (Iterator)o;
/* 130 */       if (iterator.hasNext()) {
/* 131 */         if (isObject) {
/* 132 */           return JsonToken.NAME;
/*     */         }
/* 134 */         push(iterator.next());
/* 135 */         return peek();
/*     */       } 
/*     */       
/* 138 */       return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
/*     */     } 
/* 140 */     if (o instanceof JsonObject)
/* 141 */       return JsonToken.BEGIN_OBJECT; 
/* 142 */     if (o instanceof JsonArray)
/* 143 */       return JsonToken.BEGIN_ARRAY; 
/* 144 */     if (o instanceof JsonPrimitive) {
/* 145 */       JsonPrimitive primitive = (JsonPrimitive)o;
/* 146 */       if (primitive.isString())
/* 147 */         return JsonToken.STRING; 
/* 148 */       if (primitive.isBoolean())
/* 149 */         return JsonToken.BOOLEAN; 
/* 150 */       if (primitive.isNumber()) {
/* 151 */         return JsonToken.NUMBER;
/*     */       }
/* 153 */       throw new AssertionError();
/*     */     } 
/* 155 */     if (o instanceof com.test.gson.JsonNull)
/* 156 */       return JsonToken.NULL; 
/* 157 */     if (o == SENTINEL_CLOSED) {
/* 158 */       throw new IllegalStateException("JsonReader is closed");
/*     */     }
/* 160 */     throw new MalformedJsonException("Custom JsonElement subclass " + o
/* 161 */         .getClass().getName() + " is not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   private Object peekStack() {
/* 166 */     return this.stack[this.stackSize - 1];
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Object popStack() {
/* 171 */     Object result = this.stack[--this.stackSize];
/* 172 */     this.stack[this.stackSize] = null;
/* 173 */     return result;
/*     */   }
/*     */   
/*     */   private void expect(JsonToken expected) throws IOException {
/* 177 */     if (peek() != expected) {
/* 178 */       throw new IllegalStateException("Expected " + expected + " but was " + 
/* 179 */           peek() + locationString());
/*     */     }
/*     */   }
/*     */   
/*     */   private String nextName(boolean skipName) throws IOException {
/* 184 */     expect(JsonToken.NAME);
/* 185 */     Iterator<?> i = (Iterator)peekStack();
/* 186 */     Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
/* 187 */     String result = (String)entry.getKey();
/* 188 */     this.pathNames[this.stackSize - 1] = skipName ? "<skipped>" : result;
/* 189 */     push(entry.getValue());
/* 190 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String nextName() throws IOException {
/* 195 */     return nextName(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public String nextString() throws IOException {
/* 200 */     JsonToken token = peek();
/* 201 */     if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
/* 202 */       throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + token + 
/* 203 */           locationString());
/*     */     }
/* 205 */     String result = ((JsonPrimitive)popStack()).getAsString();
/* 206 */     if (this.stackSize > 0) {
/* 207 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/* 209 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nextBoolean() throws IOException {
/* 214 */     expect(JsonToken.BOOLEAN);
/* 215 */     boolean result = ((JsonPrimitive)popStack()).getAsBoolean();
/* 216 */     if (this.stackSize > 0) {
/* 217 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/* 219 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void nextNull() throws IOException {
/* 224 */     expect(JsonToken.NULL);
/* 225 */     popStack();
/* 226 */     if (this.stackSize > 0) {
/* 227 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextDouble() throws IOException {
/* 233 */     JsonToken token = peek();
/* 234 */     if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
/* 235 */       throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token + 
/* 236 */           locationString());
/*     */     }
/* 238 */     double result = ((JsonPrimitive)peekStack()).getAsDouble();
/* 239 */     if (!isLenient() && (Double.isNaN(result) || Double.isInfinite(result))) {
/* 240 */       throw new MalformedJsonException("JSON forbids NaN and infinities: " + result);
/*     */     }
/* 242 */     popStack();
/* 243 */     if (this.stackSize > 0) {
/* 244 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/* 246 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextLong() throws IOException {
/* 251 */     JsonToken token = peek();
/* 252 */     if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
/* 253 */       throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token + 
/* 254 */           locationString());
/*     */     }
/* 256 */     long result = ((JsonPrimitive)peekStack()).getAsLong();
/* 257 */     popStack();
/* 258 */     if (this.stackSize > 0) {
/* 259 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/* 261 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt() throws IOException {
/* 266 */     JsonToken token = peek();
/* 267 */     if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
/* 268 */       throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token + 
/* 269 */           locationString());
/*     */     }
/* 271 */     int result = ((JsonPrimitive)peekStack()).getAsInt();
/* 272 */     popStack();
/* 273 */     if (this.stackSize > 0) {
/* 274 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/* 276 */     return result;
/*     */   }
/*     */   
/*     */   JsonElement nextJsonElement() throws IOException {
/* 280 */     JsonToken peeked = peek();
/* 281 */     if (peeked == JsonToken.NAME || peeked == JsonToken.END_ARRAY || peeked == JsonToken.END_OBJECT || peeked == JsonToken.END_DOCUMENT)
/*     */     {
/*     */ 
/*     */       
/* 285 */       throw new IllegalStateException("Unexpected " + peeked + " when reading a JsonElement.");
/*     */     }
/* 287 */     JsonElement element = (JsonElement)peekStack();
/* 288 */     skipValue();
/* 289 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 294 */     this.stack = new Object[] { SENTINEL_CLOSED };
/* 295 */     this.stackSize = 1;
/*     */   }
/*     */   
/*     */   public void skipValue() throws IOException {
/*     */     String unused;
/* 300 */     JsonToken peeked = peek();
/* 301 */     switch (peeked) {
/*     */       
/*     */       case NAME:
/* 304 */         unused = nextName(true);
/*     */       
/*     */       case END_ARRAY:
/* 307 */         endArray();
/*     */       
/*     */       case END_OBJECT:
/* 310 */         endObject();
/*     */       
/*     */       case END_DOCUMENT:
/*     */         return;
/*     */     } 
/*     */     
/* 316 */     popStack();
/* 317 */     if (this.stackSize > 0) {
/* 318 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     return getClass().getSimpleName() + locationString();
/*     */   }
/*     */   
/*     */   public void promoteNameToValue() throws IOException {
/* 330 */     expect(JsonToken.NAME);
/* 331 */     Iterator<?> i = (Iterator)peekStack();
/* 332 */     Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
/* 333 */     push(entry.getValue());
/* 334 */     push(new JsonPrimitive((String)entry.getKey()));
/*     */   }
/*     */   
/*     */   private void push(Object newTop) {
/* 338 */     if (this.stackSize == this.stack.length) {
/* 339 */       int newLength = this.stackSize * 2;
/* 340 */       this.stack = Arrays.copyOf(this.stack, newLength);
/* 341 */       this.pathIndices = Arrays.copyOf(this.pathIndices, newLength);
/* 342 */       this.pathNames = Arrays.<String>copyOf(this.pathNames, newLength);
/*     */     } 
/* 344 */     this.stack[this.stackSize++] = newTop;
/*     */   }
/*     */   
/*     */   private String getPath(boolean usePreviousPath) {
/* 348 */     StringBuilder result = (new StringBuilder()).append('$');
/* 349 */     for (int i = 0; i < this.stackSize; i++) {
/* 350 */       if (this.stack[i] instanceof JsonArray) {
/* 351 */         if (++i < this.stackSize && this.stack[i] instanceof Iterator) {
/* 352 */           int pathIndex = this.pathIndices[i];
/*     */ 
/*     */ 
/*     */           
/* 356 */           if (usePreviousPath && pathIndex > 0 && (i == this.stackSize - 1 || i == this.stackSize - 2)) {
/* 357 */             pathIndex--;
/*     */           }
/* 359 */           result.append('[').append(pathIndex).append(']');
/*     */         } 
/* 361 */       } else if (this.stack[i] instanceof JsonObject && 
/* 362 */         ++i < this.stackSize && this.stack[i] instanceof Iterator) {
/* 363 */         result.append('.');
/* 364 */         if (this.pathNames[i] != null) {
/* 365 */           result.append(this.pathNames[i]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 370 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 375 */     return getPath(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPreviousPath() {
/* 380 */     return getPath(true);
/*     */   }
/*     */   
/*     */   private String locationString() {
/* 384 */     return " at path " + getPath();
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/bind/JsonTreeReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */