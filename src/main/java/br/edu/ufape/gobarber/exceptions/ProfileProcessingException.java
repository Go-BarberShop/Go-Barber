package br.edu.ufape.gobarber.exceptions;

public class ProfileProcessingException extends RuntimeException {
    public ProfileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileProcessingException(String message) {
        super(message);
    }
}
