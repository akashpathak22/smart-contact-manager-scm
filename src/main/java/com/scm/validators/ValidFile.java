package com.scm.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;



@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,ElementType.CONSTRUCTOR,ElementType.ANNOTATION_TYPE })
@Constraint(validatedBy = FileValidator.class)

public @interface ValidFile {

  String message () default "invalid file";
  Class<?>[] groups() default{};
  Class<? extends Payload> [] payload() default{};

}
