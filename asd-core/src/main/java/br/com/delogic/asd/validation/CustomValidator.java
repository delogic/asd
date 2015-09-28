package br.com.delogic.asd.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.delogic.asd.exception.UnexpectedApplicationException;

/**
 * Implementação de validator que irá utilizar todos os validators previamente
 * configurados e executá-los sequencialmente validando o objeto em questão.
 *
 * @author celio@delogic.com.br
 *
 */
public class CustomValidator implements ConstraintValidator<CustomValidation, Object> {

    private CustomValidation annotation;

    @Override
    public void initialize(CustomValidation constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        Class<? extends ConstraintValidator<CustomValidation, ? extends Object>>[] types = annotation.value();
        for (Class<? extends ConstraintValidator<CustomValidation, ? extends Object>> type : types) {
            ConstraintValidator<CustomValidation, Object> validator = newInstance(type);
            validator.initialize(annotation);

            if (!validator.isValid(value, context)) {
                isValid = false;
            }
        }

        return isValid;
    }

    @SuppressWarnings("unchecked")
    private ConstraintValidator<CustomValidation, Object> newInstance(
        Class<? extends ConstraintValidator<CustomValidation, ? extends Object>> type) {
        try {
            return (ConstraintValidator<CustomValidation, Object>) type.newInstance();
        } catch (Exception e) {
            throw new UnexpectedApplicationException("Erro ao tentar criar validator do tipo:" + type, e);
        }
    }

}
