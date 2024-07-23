package com.test.gson;

import com.test.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
  <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken);
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/TypeAdapterFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */