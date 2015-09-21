package br.com.delogic.asd.exception;

@SuppressWarnings("serial")
public class ContentException extends AsdRuntimeException {

    public ContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentException(String message) {
        super(message);
    }

    public ContentException() {

    }

}
