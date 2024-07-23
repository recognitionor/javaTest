package com.test.gson;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {
  T createInstance(Type paramType);
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/InstanceCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */