package Controllers;

import Models.Discount;
import Models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ManagerController extends UserController{
    GlobalVariables userVariables;
    private HashMap<Integer,String> giftEvents;
    private HashMap<Integer,String> giftActions;
    public ManagerController(GlobalVariables userVariables) {
        super(userVariables);
        giftEvents = new HashMap<>();
        giftActions = new HashMap<>();
        giftActions.put(1,"discount in current log");
        giftActions.put(2, "give discount code");
        giftEvents.put(1," first buy gift");
        giftEvents.put(2,"high log price event");
        giftEvents.put(3," periodic gift");

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

    public HashMap<Integer,String> getGiftEventsName(){
        return this.giftEvents;
    }

    public HashMap<Integer,String> getGiftActionsName(){
        return this.giftActions;
    }

    public void createGift(int event,int action){

    }


    public static class InvalidDiscountIdException extends Exception {
        public InvalidDiscountIdException(String message) {
            super(message);
        }
    }
}
