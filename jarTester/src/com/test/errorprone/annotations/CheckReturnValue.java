package com.test.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReturnValue {}


/* Location:              /Users/nhn/Downloads/error_prone_annotations-2.28.0.jar!/com/google/errorprone/annotations/CheckReturnValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */