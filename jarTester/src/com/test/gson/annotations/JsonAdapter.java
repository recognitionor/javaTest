package com.test.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface JsonAdapter {
  Class<?> value();
  
  boolean nullSafe() default true;
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/annotations/JsonAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */