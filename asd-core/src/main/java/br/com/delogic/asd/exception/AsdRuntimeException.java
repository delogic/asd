package br.com.delogic.asd.exception;

import org.springframework.util.Assert;

import br.com.delogic.jfunk.Has;

/**
 * Exception base para todas as exceptions do ASD.
 *
 * @author Célio
 *
 */
@SuppressWarnings("serial")
public class AsdRuntimeException extends RuntimeException {

    private AsdRuntimeException parent;

    public AsdRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsdRuntimeException(String message) {
        super(message);
    }

    public AsdRuntimeException() {
        super();
    }

    /**
     * Retorna uma exception que faz equals com o tipo principal, permitindo
     * comparação com uma única instância.
     *
     * @param message
     * @param params
     * @return
     */
    private AsdRuntimeException withMessage(String message, Object... params) {
        AsdRuntimeException ex = new AsdRuntimeException(Has.content(params) ? String.format(message, params) : message) {
            @Override
            public boolean equals(Object obj) {
                return AsdRuntimeException.this == obj;
            }
        };
        ex.parent = this;
        return ex;
    }

    public AsdRuntimeException throwIf(boolean condition) throws AsdRuntimeException {
        Assert.hasText(getMessage());
        if (condition) {
            throw this;
        }
        return this;
    }

    public AsdRuntimeException throwIf(boolean condition, String message, Object... params) throws AsdRuntimeException {
        Assert.hasText(message);
        if (condition) {
            throw withMessage(message, params);
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AsdRuntimeException)) {
            return false;
        }
        AsdRuntimeException that = (AsdRuntimeException) obj;
        return this == that || this == that.parent;
    }

    public boolean is(AsdRuntimeException ex) {
        return equals(ex);
    }

}
