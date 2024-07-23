package com.test.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T> {
  JsonElement serialize(T paramT, Type paramType, JsonSerializationContext paramJsonSerializationContext);
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */