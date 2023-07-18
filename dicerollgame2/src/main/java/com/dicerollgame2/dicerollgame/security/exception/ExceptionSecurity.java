package com.dicerollgame2.dicerollgame.security.exception;

public class ExceptionSecurity extends RuntimeException {
    public ExceptionSecurity(String message) {
        super(message);
    }

    public static class EmailAlreadyExistsException extends ExceptionSecurity {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends ExceptionSecurity {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    public static class ForbiddenException extends ExceptionSecurity {
        public ForbiddenException(String message) {
            super(message);
        }
    }
    public  static class NotFoundException extends ExceptionSecurity {
        public NotFoundException(String message) {
            super(message);
        }
    }
}