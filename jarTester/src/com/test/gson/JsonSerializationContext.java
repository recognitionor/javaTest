package com.test.gson;

import java.lang.reflect.Type;

public interface JsonSerializationContext {
  JsonElement serialize(Object paramObject);
  
  JsonElement serialize(Object paramObject, Type paramType);
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/JsonSerializationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */