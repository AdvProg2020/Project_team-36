package View;

import Models.Discount;

import java.util.HashMap;

public class CustomerMenu extends Menu{
    public CustomerMenu(String name, Menu parentMenu) {
        super("customerMenu", parentMenu);
        subMenus.put("view\\s+personal\\s+info",null);
        subMenus.put("view\\s+cart",new CartMenu("cartMenu",this));
        subMenus.put("view\\s+discount\\s+codes",getDiscountCodesMenu());
        subMenus.put("view\\s+balance",getBalanceMenu());
        subMenus.put("view\\s+orders",new OrderMenu("orderMenu",this));

    }

    @Override
    public void help() {
        System.out.println("view personal info\nview cart\nview orders\nview balance\nview discount codes");
    }

    private Menu getDiscountCodesMenu(){
        return new Menu("DiscountCodeMenu",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                HashMap<Discount, Integer> discounts = customerController.getDiscountCodes();
                if(discounts.isEmpty()){
                    System.out.println("You have no available discount!");
                    this.getParentMenu().execute();
                }
                System.out.println("Discount code    Repetition    EndTime");
                for (Discount discount : discounts.keySet()) {
                    System.out.printf("%10s%10d%20s",discount.getId(),discounts.get(discount),discount.getEndTime());
                    System.out.println();
                }
                this.getParentMenu().execute();
            }
        };
    }

    private Menu getBalanceMenu(){
        return new Menu("showBalance",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                System.out.println("your total balance is: "+customerController.getBalance());
                this.getParentMenu().execute();
            }
        };
    }


}