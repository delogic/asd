package br.com.delogic.asd.validation;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.delogic.asd.content.BaseTest;

public class CustomValidatorTest extends BaseTest {

    private CustomValidator validator;
    @Mock
    private CustomValidation mockCustomValidation;
    @Mock
    private ConstraintValidatorContext mockConstraintValidator;
    @Mock
    private Object mockValue;
    private boolean isValid;
    public static boolean called;
    public static Object calledValue;
    public static ConstraintValidatorContext calledContext;

    public static class MyValidator extends BaseValidator<Object> {

        public static boolean valid;
        public static int calledTimes = 0;

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            called = true;
            calledValue = value;
            calledContext = context;
            calledTimes++;
            return valid;
        }

    }

    @Before
    public void init() {
        this.validator = new CustomValidator();
        called = false;
        calledContext = null;
        calledValue = null;
        MyValidator.valid = false;
        MyValidator.calledTimes = 0;
    }

    @Test
    public void shouldRunValidationTrue() {
        givenValidator(true);
        whenValidating();
        thenValidationReturns(true);
        thenValidatorIsCalled(1);
        thenValueIsPassed(mockValue);
        thenContextIsPassed();
    }

    @Test
    public void shouldRunValidationFalse() {
        givenValidator(false);
        whenValidating();
        thenValidationReturns(false);
        thenValidatorIsCalled(1);
        thenValueIsPassed(mockValue);
        thenContextIsPassed();
    }

    @Test
    public void shouldRunManyValidators() {
        givenValidators();// 3
        whenValidating();
        thenValidationReturns(false);
        thenValidatorIsCalled(3);
        thenValueIsPassed(mockValue);
        thenContextIsPassed();
    }

    private void givenValidators() {
        validator.initialize(mockCustomValidation);
        Mockito.when(mockCustomValidation.value()).thenReturn(new Class[] { MyValidator.class, MyValidator.class, MyValidator.class });
    }

    private void thenValidationReturns(boolean b) {
        assertEquals(b, isValid);
    }

    @SuppressWarnings("unchecked")
    private void givenValidator(boolean b) {
        validator.initialize(mockCustomValidation);
        Mockito.when(mockCustomValidation.value()).thenReturn(new Class[] { MyValidator.class });
        MyValidator.valid = b;
    }

    private void whenValidating() {
        isValid = validator.isValid(mockValue, mockConstraintValidator);
    }

    private void thenValidatorIsCalled(int i) {
        assertTrue(called);
        assertEquals(i, MyValidator.calledTimes);
    }

    private void thenValueIsPassed(Object value) {
        assertEquals(value, calledValue);
    }

    private void thenContextIsPassed() {
        assertNotNull(calledContext);
        Mockito.verify(calledContext).disableDefaultConstraintViolation();
    }

}
