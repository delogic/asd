package br.com.delogic.asd.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Serviço de validação que pode ser injetado em controllers e services.
 *
 * @author celio@delogic.com.br
 *
 */
public interface ValidationManager {

    /**
     * Apenas realiza a validação e retorna uma lista de violações caso existam.
     *
     * @param object
     * @return
     */
    <T> Set<ConstraintViolation<T>> validate(T object);

    /**
     * Faz a validação e lança uma exception com violações caso existam.
     *
     * @param object
     * @throws ConstraintViolationException
     */
    <T> void validateAndThrow(T object) throws ConstraintViolationException;

}
