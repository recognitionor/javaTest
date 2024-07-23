package com.test.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.lang.model.element.Modifier;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE})
public @interface RequiredModifiers {
  @Deprecated
  Modifier[] value() default {};
  
  Modifier[] modifier() default {};
}


/* Location:              /Users/nhn/Downloads/error_prone_annotations-2.28.0.jar!/com/google/errorprone/annotations/RequiredModifiers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */