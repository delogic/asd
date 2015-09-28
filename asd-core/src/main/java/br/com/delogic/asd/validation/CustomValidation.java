package br.com.delogic.asd.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

/**
 * Anotação para expor diversos validators customizados para uma única classe,
 * permitindo validação através de diversas propriedades.
 *
 * @author celio@delogic.com.br
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { CustomValidator.class })
public @interface CustomValidation {

    Class<? extends ConstraintValidator<CustomValidation, ? extends Object>>[] value();

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}