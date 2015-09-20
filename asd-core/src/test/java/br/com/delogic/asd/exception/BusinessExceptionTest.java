package br.com.delogic.asd.exception;

import org.junit.Assert;
import org.junit.Test;

public class BusinessExceptionTest extends Assert {

    private BusinessException ex;
    private BusinessException thrown;

    @Test
    public void shouldThrowException() {
        givenBusinessException("Test");
        whenCheckingIfShouldThrow(true);
        thenExceptionIsThrown();
        thenExceptionIsTheSame(ex, thrown);
        thenMessageIs("Test");
    }

    private void thenMessageIs(String string) {
        assertEquals(string, thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithMessage() {
        givenBusinessException("Test");
        whenCheckingIfShouldThrowWithMessage(true, "Another message");
        thenExceptionIsThrown();
        thenExceptionIsTheSame(ex, thrown);
        thenMessageIs("Another message");
    }

    @Test
    public void shouldThrowExceptionWithMessageFormatted() {
        givenBusinessException("Test");
        whenCheckingIfShouldThrowWithMessage(true, "Another message %s %s", "goes", "here");
        thenExceptionIsThrown();
        thenExceptionIsTheSame(ex, thrown);
        thenMessageIs("Another message goes here");
    }

    private void whenCheckingIfShouldThrowWithMessage(boolean b, String string, Object... params) {
        thrown = null;
        try {
            ex.throwIf(b, string, params);
        } catch (BusinessException e) {
            thrown = e;
        }

    }

    private void thenExceptionIsThrown() {
        assertNotNull(thrown);
    }

    private void givenBusinessException(String string) {
        ex = BusinessException.of(string);
    }

    private void whenCheckingIfShouldThrow(boolean b) {
        thrown = null;
        try {
            ex.throwIf(b);
        } catch (BusinessException e) {
            thrown = e;
        }
    }

    private void thenExceptionIsTheSame(BusinessException declared, BusinessException justThrown) {
        assertTrue(declared.equals(justThrown));
        assertTrue(justThrown.equals(declared));
        assertTrue(declared.is(justThrown));
        assertTrue(justThrown.is(declared));
    }

    @Test
    public void shouldNotThrowException() {
        givenBusinessException("Test2");
        whenCheckingIfShouldThrow(false);
        thenNoExceptionIsThrown();
    }

    private void thenNoExceptionIsThrown() {
        assertNull(thrown);
    }

}
