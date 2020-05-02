package View;

import Controllers.DiscountController;
import Controllers.ManagerController;
import Models.Customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerMenu extends Menu {

    public ManagerMenu(Menu parentMenu) {
        super("ManagerMenu",parentMenu);
        //  subMenus.put("view\\s+personal\\s+info\\s+menu", new ViewPersonalInfoMenu(this));
//      subMenus.put("manage\\s+categories", new ManageCategoriesMenu(this));
//      subMenus.put("manage\\s+requests", new ManageRequestsMenu(this));
      subMenus.put("view\\s+discount\\s+codes", new ViewDiscountCodesMenu(this));
//      subMenus.put("manage\\s+users", new ManageUsersMenu(this));
//      subMenus.put("manage\\s+all\\s+products", new ManageAllProductsMenu(this));
        subMenus.put("create\\s+discount\\s+code",createDiscountCode());

    }

    private Menu createDiscountCode(){
        return new Menu("create discount code",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                    DiscountController discountController = new DiscountController();
                    System.out.println("please enter the required information:");
                    setStartDateForDiscount(discountController);
                    setEndDateForDiscount(discountController);
                    setDiscountPercentage(discountController);
                    setDiscountLimit(discountController);
                    setUsageFrequency(discountController);
                    setUsersIncludedForCode(discountController);
                    discountController.finalizeTheNewDiscountCode();
                    this.execute();
            }
        };
    }

    private void setStartDateForDiscount(DiscountController discountController){
        String input;
        while(true) {
            System.out.println("start date in yyyy/MM/dd format :");
            input = scanner.nextLine();
            if(input.equals("back")){
                this.parentMenu.execute();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date startDate;
            try {
                startDate = dateFormat.parse(input);
                discountController.setStartTime(startDate);
                return;
            } catch (ParseException e) {
                System.err.println("input isn't in the yyyy/MM/dd format");
            }
        }
    }

    private void setEndDateForDiscount(DiscountController discountController){
        String input;
        while(true) {
            System.out.println("end date in yyyy/MM/dd format:");
            input = scanner.nextLine();
            if(input.equals("back")){
                this.parentMenu.execute();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date endDate;
            try {
                endDate = dateFormat.parse(input);
                try {
                    discountController.setEndTime(endDate);
                    return;
                }catch (DiscountController.InvalidDateException dateError){
                    System.err.println(dateError.getMessage());
                }
            } catch (ParseException formatError) {
                System.err.println("input isn't in the yyyy/MM/dd format");
            }
        }
    }

    private void  setDiscountPercentage(DiscountController discountController){
        int percentage;
        String input;
        while (true){
            System.out.println("discount percentage between 0-100 :");
            try {
                input=scanner.nextLine();
                if(input.equals("back")){
                    this.parentMenu.execute();
                }
                percentage = Integer.parseInt(input);
                if (percentage < 100 && percentage > 0) {
                    discountController.setDiscountPercent(percentage * 0.01);
                    return;
                } else {
                    System.err.println("number not in the desired range");
                }
            }catch (NumberFormatException e){
                System.err.println("you can't enter anything but number");
            }
        }
    }

    private void  setDiscountLimit(DiscountController discountController){
        long limit;
        String input;
        while (true){
            System.out.println("discount limit :");
            try {
                input=scanner.nextLine();
                if(input.equals("back")){
                    this.parentMenu.execute();
                }
                limit = Long.parseLong(input);
                discountController.setDiscountLimit(limit);
                return;
            }catch (NumberFormatException e){
                System.err.println("you can't enter anything but number");
            }
        }
    }

    private void  setUsageFrequency(DiscountController discountController){
        int frequency;
        String input;
        while (true){
            System.out.println("number of times a customer can use this code :");
            try {
                input = scanner.nextLine();
                if(input.equals("back")){
                    this.parentMenu.execute();
                }
                frequency = Integer.parseInt(input);
                discountController.setRepetitionForEachUser(frequency);
                return;
            }catch (NumberFormatException e){
                System.err.println("you can't enter anything but number");
            }
        }
    }

    private void  setUsersIncludedForCode(DiscountController discountController){
        System.out.println("choose the customers who can use this code and when you are done enter end :");
        String input;
        int number=1;
        for (Customer customer : discountController.getAllCustomers()) {
            System.out.println(number+") "+customer.getUsername());
            number++;
        }
        while (!(input=scanner.nextLine()).equalsIgnoreCase("end")){
            try {
                discountController.setCustomersForDiscountCode(input);
            } catch (DiscountController.InvalidUsernameException e){
                System.err.println(e.getMessage());
            }
        }
    }


        @Override
    public void help() {

    }
}




