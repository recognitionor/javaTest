package com.test.gson;

import java.lang.reflect.Type;

public interface JsonDeserializationContext {
  <T> T deserialize(JsonElement paramJsonElement, Type paramType) throws JsonParseException;
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonDeserializationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */