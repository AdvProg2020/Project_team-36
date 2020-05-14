package Exceptions;

public class NoProductWithThisIdException extends Exception{
    public NoProductWithThisIdException(String message) {
        super(message);
    }
}
