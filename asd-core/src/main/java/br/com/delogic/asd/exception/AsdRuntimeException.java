package br.com.delogic.asd.exception;

@SuppressWarnings("serial")
public class AsdRuntimeException extends Exception {

    public AsdRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsdRuntimeException(String message) {
        super(message);
    }

}
