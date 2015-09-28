package br.com.delogic.asd.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;
import org.junit.Test;

import br.com.delogic.asd.content.BaseTest;
import br.com.delogic.jfunk.Has;

public class ValidationManagerImplTest extends BaseTest {

    private Person person;
    private ValidationManagerImpl validator;
    private Set<ConstraintViolation<Person>> constraints;

    @Before
    public void init() {
        validator = new ValidationManagerImpl();
    }

    @Test
    public void shouldValidateOnlyNoViolations() {
        givenObject("value");
        whenValidating();
        thenNoConstraintsAreReturned();
    }

    @Test
    public void shouldValidateOnlyViolationReturned() {
        givenObject("");
        whenValidating();
        thenConstraintsAreReturned();
    }

    private void thenConstraintsAreReturned() {
        assertEquals(1, constraints.size());
    }

    private void givenObject(String string) {
        person = new Person();
        person.name = string;
    }

    private void whenValidating() {
        constraints = validator.validate(person);
    }

    private void thenNoConstraintsAreReturned() {
        assertTrue(!Has.content(constraints));
    }

    public static class Person {

        @NotEmpty
        private String name;

    }

    @Test
    public void shouldValidateAndThrowNoViolations() {
        givenObject("test");
        whenValidatingAndThrow();
        thenSuccess();
    }

    private void whenValidatingAndThrow() {
        validator.validateAndThrow(person);
    }

    private void thenSuccess() {
        System.out.println("success");
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldValidateAndThrowViolationsReturned() {
        givenObject(null);
        whenValidatingAndThrow();
        fail("Should throw exception");
    }

}
