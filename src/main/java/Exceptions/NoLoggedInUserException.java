package Exceptions;

public class NoLoggedInUserException extends Exception{
    public NoLoggedInUserException(String message) {
        super(message);
    }
}
