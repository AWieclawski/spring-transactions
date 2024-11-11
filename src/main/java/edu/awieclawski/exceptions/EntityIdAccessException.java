package edu.awieclawski.exceptions;

public class EntityIdAccessException extends RuntimeException {

    private static final long serialVersionUID = -3215303318396233061L;
    public EntityIdAccessException() {
    }

    public EntityIdAccessException(String message) {
        super(message);
    }

    public EntityIdAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityIdAccessException(Throwable cause) {
        super(cause);
    }

    public EntityIdAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
