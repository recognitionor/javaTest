package com.test.errorprone.annotations.concurrent;

import com.test.errorprone.annotations.IncompatibleModifiers;
import com.test.errorprone.annotations.Modifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@IncompatibleModifiers(modifier = {Modifier.FINAL})
public @interface LazyInit {}


/* Location:              /Users/nhn/Downloads/error_prone_annotations-2.28.0.jar!/com/google/errorprone/annotations/concurrent/LazyInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */