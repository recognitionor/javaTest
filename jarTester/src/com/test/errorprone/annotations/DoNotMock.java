package com.test.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface DoNotMock {
  String value() default "Create a real instance instead";
}


/* Location:              /Users/nhn/Downloads/error_prone_annotations-2.28.0.jar!/com/google/errorprone/annotations/DoNotMock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */