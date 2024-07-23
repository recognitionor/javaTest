package com.test.errorprone.annotations.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy {
  String value();
}


/* Location:              /Users/nhn/Downloads/error_prone_annotations-2.28.0.jar!/com/google/errorprone/annotations/concurrent/GuardedBy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */