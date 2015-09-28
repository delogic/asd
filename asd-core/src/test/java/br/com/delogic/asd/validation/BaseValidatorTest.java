package br.com.delogic.asd.validation;

import javax.validation.ConstraintValidatorContext;

import org.junit.Test;
import org.mockito.Mock;

import br.com.delogic.asd.content.BaseTest;

public class BaseValidatorTest extends BaseTest {

    private BaseValidator<String> instance;
    @Mock
    private CustomValidation mockCustomValidation;

    @Test
    public void shouldInitilizeAnnotation() {
        givenInstanceOfBaseValidator();
        whenInitilizing();
        thenAnnotationIsPresent();
    }

    private void givenInstanceOfBaseValidator() {
        instance = new BaseValidator<String>() {
            @Override
            public boolean isValid(String value, ConstraintValidatorContext context) {
                return false;
            }
        };
    }

    private void whenInitilizing() {
        instance.initialize(mockCustomValidation);
    }

    private void thenAnnotationIsPresent() {
        assertNotNull(instance.annotation);
    }

}
