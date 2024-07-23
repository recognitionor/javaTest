package com.test.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface MustBeClosed {}


/* Location:              /Users/nhn/Downloads/error_prone_annotations-2.28.0.jar!/com/google/errorprone/annotations/MustBeClosed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */