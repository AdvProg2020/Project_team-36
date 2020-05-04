package View;

import Controllers.CustomerController;

public class PurchaseMenu extends Menu {

    public PurchaseMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
    }

    @Override
    public void help() {
        System.out.println("purchase");
    }

    @Override
    public void execute() {
        String input;
        while(!(input = scanner.nextLine().trim()).matches("purchase")){
            if(input.matches("help"))
                help();
            else if(input.matches("back"))
                this.parentMenu.execute();
            else
                System.err.println("invalid command!Try again please");
        }
        try {
            customerController.purchase();
            System.out.println("Purchased successfully\n Returning to cartMenu...");
        }catch(CustomerController.NotEnoughMoney e){
            System.err.println("Not enough money in your account! try again after charging your account!\nReturning to cart menu...");
            return;
        }
    }
}
