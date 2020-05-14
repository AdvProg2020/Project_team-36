package Exceptions;

public class NoProductForThisSellerException extends Exception{
    public NoProductForThisSellerException(String message) {
        super(message);
    }
}
