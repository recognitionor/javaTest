/*     */ package com.test.gson;
/*     */ 
/*     */ import com.test.errorprone.annotations.InlineMe;
/*     */ import com.test.gson.internal.Streams;
/*     */ import com.test.gson.stream.JsonReader;
/*     */ import com.test.gson.stream.JsonToken;
/*     */ import com.test.gson.stream.MalformedJsonException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
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
/*     */ public final class JsonParser
/*     */ {
/*     */   public static JsonElement parseString(String json) throws JsonSyntaxException {
/*  92 */     return parseReader(new StringReader(json));
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
/*     */   public static JsonElement parseReader(Reader reader) throws JsonIOException, JsonSyntaxException {
/*     */     try {
/* 109 */       JsonReader jsonReader = new JsonReader(reader);
/* 110 */       JsonElement element = parseReader(jsonReader);
/* 111 */       if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
/* 112 */         throw new JsonSyntaxException("Did not consume the entire document.");
/*     */       }
/* 114 */       return element;
/* 115 */     } catch (MalformedJsonException e) {
/* 116 */       throw new JsonSyntaxException(e);
/* 117 */     } catch (IOException e) {
/* 118 */       throw new JsonIOException(e);
/* 119 */     } catch (NumberFormatException e) {
/* 120 */       throw new JsonSyntaxException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonElement parseReader(JsonReader reader) throws JsonIOException, JsonSyntaxException {
/* 140 */     Strictness strictness = reader.getStrictness();
/* 141 */     if (strictness == Strictness.LEGACY_STRICT)
/*     */     {
/* 143 */       reader.setStrictness(Strictness.LENIENT);
/*     */     }
/*     */     try {
/* 146 */       return Streams.parse(reader);
/* 147 */     } catch (StackOverflowError e) {
/* 148 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/* 149 */     } catch (OutOfMemoryError e) {
/* 150 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/*     */     } finally {
/* 152 */       reader.setStrictness(strictness);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "JsonParser.parseString(json)", imports = {"com.google.gson.JsonParser"})
/*     */   public JsonElement parse(String json) throws JsonSyntaxException {
/* 162 */     return parseString(json);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "JsonParser.parseReader(json)", imports = {"com.google.gson.JsonParser"})
/*     */   public JsonElement parse(Reader json) throws JsonIOException, JsonSyntaxException {
/* 171 */     return parseReader(json);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "JsonParser.parseReader(json)", imports = {"com.google.gson.JsonParser"})
/*     */   public JsonElement parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
/* 180 */     return parseReader(json);
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */