package View;

import Controllers.CustomerController;
import Exceptions.NoLoggedInUserException;
import Models.Gifts.Gift;
import Models.SelectedItem;

import java.util.ArrayList;

public class PurchaseMenu extends Menu {

    public PurchaseMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
    }

    @Override
    public void help() {
        System.out.println("purchase\n" +
                "back\n" +
                "logout");
    }

    @Override
    public void execute() {
        try {
            String input;
            System.out.println("The available items you are buying with their counts:");
            ArrayList<SelectedItem> items = customerController.getWaitingLogItems();
            int i =1;
            for (SelectedItem item : items) {
                System.out.println(i+"."+item.getProduct().getName()+"   "+item.getCount()+"   "+item.getItemTotalPrice()+"RIALS");
            }
            while(!(input = scanner.nextLine().trim()).matches("purchase")){
                if(input.matches("help"))
                    help();
                else if(input.matches("back")) {
                    customerController.cancelPurchase();
                    this.parentMenu.execute();
                }
                else if(input.matches("logout")) {
                    customerController.cancelPurchase();
                    logoutChangeMenu();
                }
                else
                    System.err.println("invalid command!Try again please");
            }
            ArrayList<Gift> gifts =  customerController.getGifts();
            System.out.println("Gifts available for your purchase:");
            if(gifts.isEmpty())
                System.out.println("No gifts!");
            for (Gift gift : gifts) {
                System.out.println(gift.toString());
            }
            customerController.purchase();
            System.out.println("Purchased successfully\n Returning to cartMenu...");
        } catch(CustomerController.NotEnoughMoney e){
            System.err.println("Not enough money in your account!\nYou need "+ e.getAmount()+" rials!\ntry again after recharging your account!" +
                    "\nReturning to cart menu...");
            return;
        }catch (NoLoggedInUserException e){
            System.err.println(e.getMessage());
        }
    }
}
