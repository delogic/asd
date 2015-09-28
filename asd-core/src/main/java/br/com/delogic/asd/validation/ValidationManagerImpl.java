package br.com.delogic.asd.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import br.com.delogic.jfunk.Has;

/**
 * Implementação base para a validação. Cria um validador ou pode receber um
 * validador customizado caso necessário.
 *
 * @author celio@delogic.com.br
 *
 */
public class ValidationManagerImpl implements ValidationManager {

    private final Validator validator;

    public ValidationManagerImpl(Validator validator) {
        this.validator = validator;
    }

    public ValidationManagerImpl() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }

    @Override
    public <T> void validateAndThrow(T object) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (Has.content(violations)) {
            throw new ConstraintViolationException(violations);
        }
    }

}
