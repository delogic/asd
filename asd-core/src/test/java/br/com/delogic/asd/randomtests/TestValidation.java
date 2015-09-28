package br.com.delogic.asd.randomtests;

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
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.delogic.jfunk.Has;

public class TestValidation extends Assert {

    private Validator validator;

    @MinhaValidacao
    public static class Person {

        @NotEmpty
        private String name;

        @Valid
        private Pet pet;

        public Person(String name) {
            this.name = name;
            if (Has.content(name)) {
                name += "'s Pet";
            }
            pet = new Pet(name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Pet getPet() {
            return pet;
        }

        public void setPet(Pet pet) {
            this.pet = pet;
        }

    }

    public static class Pet {

        private String name;

        public Pet(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @Before
    public void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void shouldValidate() {
        Set<ConstraintViolation<Person>> violacoes = validator.validate(new Person(""));
        toString(violacoes);
    }

    private void toString(Set<ConstraintViolation<Person>> violacoes) {
        for (ConstraintViolation<Person> constraintViolation : violacoes) {
            System.err.println(ToStringBuilder.reflectionToString(constraintViolation, ToStringStyle.MULTI_LINE_STYLE));
        }
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, ElementType.TYPE })
    @Retention(RUNTIME)
    @Documented
    @Constraint(validatedBy = { MeuValidator.class })
    public static @interface MinhaValidacao {

        String message() default "";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    public static class MeuValidator implements ConstraintValidator<MinhaValidacao, Person> {

        private MinhaValidacao anotacao;

        @Override
        public void initialize(MinhaValidacao constraintAnnotation) {
            this.anotacao = constraintAnnotation;
        }

        @Override
        public boolean isValid(Person value, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{another.template}")
                .addPropertyNode("pet2")
                .addPropertyNode("name")
                .addConstraintViolation();
            return false;
        }

    }

}
