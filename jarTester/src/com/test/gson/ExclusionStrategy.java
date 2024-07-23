package com.test.gson;

public interface ExclusionStrategy {
  boolean shouldSkipField(FieldAttributes paramFieldAttributes);
  
  boolean shouldSkipClass(Class<?> paramClass);
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/ExclusionStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */