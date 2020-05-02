package Controllers;

import Models.Discount;
import Models.User;

import java.util.ArrayList;

public class ManagerController extends UserController{
    GlobalVariables userVariables;
    public ManagerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public ArrayList<User> getAllUsers(){
        return User.getAllUsers();
    }

    public ArrayList<Discount> getAllDiscountCodes(){
        return Discount.getAllDiscounts();
    }

    public Discount getDiscountWithId(int id) throws InvalidDiscountIdException{
        if(Discount.isThereDiscountWithId(id)){
            return Discount.getDiscountWithId(id);
        } else {
            throw new ManagerController.InvalidDiscountIdException("there is no discount with this id");
        }
    }

    public void removeDiscount(int id) throws InvalidDiscountIdException{
        if(Discount.isThereDiscountWithId(id)){
            Discount.getDiscountWithId(id).removeDiscount();
        } else {
            throw new ManagerController.InvalidDiscountIdException("there is no discount with this id");
        }
    }


    public static class InvalidDiscountIdException extends Exception {
        public InvalidDiscountIdException(String message) {
            super(message);
        }
    }
}
