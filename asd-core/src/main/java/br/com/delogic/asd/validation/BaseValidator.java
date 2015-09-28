package br.com.delogic.asd.validation;

import javax.validation.ConstraintValidator;

/**
 * Base class to extend and create your own custom validators. Cada validador
 * deverá escrever suas próprias regras de validação e incluir as constraints
 * necessárias através do método
 * <p>
 * context.buildConstraintViolationWithTemplate("{another.template}")
 * .addPropertyNode
 * ("property1").addPropertyNode("subproperty1").addConstraintViolation();
 *
 * @author celio@delogic.com.br
 *
 * @param <T>
 */
public abstract class BaseValidator<T> implements ConstraintValidator<CustomValidation, T> {

    protected CustomValidation annotation;

    @Override
    public void initialize(CustomValidation constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

}
