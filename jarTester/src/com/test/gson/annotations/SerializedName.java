package com.test.gson.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializedName {
  String value();
  
  String[] alternate() default {};
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/annotations/SerializedName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */