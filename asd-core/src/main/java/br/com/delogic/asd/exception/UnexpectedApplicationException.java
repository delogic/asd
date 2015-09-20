package br.com.delogic.asd.exception;

@SuppressWarnings("serial")
public class UnexpectedApplicationException extends AsdRuntimeException {

    public UnexpectedApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedApplicationException(String message) {
        super(message);
    }

}
