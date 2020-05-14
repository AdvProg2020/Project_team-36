package Exceptions;

public class NoLoggedInSellerException extends Exception{
    public NoLoggedInSellerException(String message) {
        super(message);
    }
}
