package br.com.delogic.asd.exception;

@SuppressWarnings("serial")
public class AsdException extends Exception {

    public AsdException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsdException(String message) {
        super(message);
    }

}
