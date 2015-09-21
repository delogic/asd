package br.com.delogic.asd.exception;

import org.springframework.util.Assert;

import br.com.delogic.jfunk.Has;

/**
 * Exception que será lançada caso alguma processamento precise ser interrompido
 * porém não é uma regra de negócio.
 *
 * @author Célio
 *
 */
@SuppressWarnings("serial")
public class ProcessException extends AsdException {

    private ProcessException parent;

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException() {
        super(null);
    }

    public static ProcessException of(String message) {
        return new ProcessException(message);
    }

    /**
     * Retorna uma exception que faz equals com o tipo principal, permitindo
     * comparação com uma única instância.
     *
     * @param message
     * @param params
     * @return
     */
    private ProcessException withMessage(String message, Object... params) {
        ProcessException ex = new ProcessException(Has.content(params) ? String.format(message, params) : message) {
            @Override
            public boolean equals(Object obj) {
                return ProcessException.this == obj;
            }
        };
        ex.parent = this;
        return ex;
    }

    public ProcessException thrownIf(boolean condition) throws ProcessException {
        Assert.hasText(getMessage());
        if (condition) {
            throw this;
        }
        return this;
    }

    public ProcessException thrownIf(boolean condition, String message, Object... params) throws ProcessException {
        Assert.hasText(message);
        if (condition) {
            throw withMessage(message, params);
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProcessException)) {
            return false;
        }
        ProcessException that = (ProcessException) obj;
        return this == that || this == that.parent;
    }

    public boolean is(ProcessException ex) {
        return equals(ex);
    }

}
