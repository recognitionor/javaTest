package com.test.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer<T> {
  T deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException;
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */