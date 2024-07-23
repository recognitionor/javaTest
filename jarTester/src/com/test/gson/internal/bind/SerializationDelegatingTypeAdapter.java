package com.test.gson.internal.bind;

import com.test.gson.TypeAdapter;

public abstract class SerializationDelegatingTypeAdapter<T> extends TypeAdapter<T> {
  public abstract TypeAdapter<T> getSerializationDelegate();
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/bind/SerializationDelegatingTypeAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */