package Controllers;

public class ProductsController {

    //todo
    public static class NoProductWithId extends Exception {
    }

    public static class NotInTheSameCategory extends Exception {
    }

    public static class NoSortException extends Exception {
    }

    public static class NoFilterWithNameException extends Exception {
    }

    public static class IntegerFieldException extends Exception {
    }

    public static class OptionalFieldException extends Exception {
    }

    public static class NoCategoryWithName extends Exception {
    }

    public static class NotEnoughSupply extends Exception {
    }

    public static class NoSellerWithUsername extends Exception {
    }

    public static class NoSellerIsChosen extends Exception {
    }

    public static class UserCantBuy extends Exception {

    }
}
