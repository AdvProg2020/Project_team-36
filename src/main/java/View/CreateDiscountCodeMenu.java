package View;

import Controllers.DiscountController;
import Models.Customer;
import Models.Discount;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CreateDiscountCodeMenu extends Menu{

    public CreateDiscountCodeMenu(Menu parentMenu) {
        super("create discount code", parentMenu);
    }

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

    public Discount newCodeForGift(){
        DiscountController discountController = new DiscountController();
        System.out.println("please enter the required information:");
        setStartDateForDiscount(discountController);
        setEndDateForDiscount(discountController);
        setDiscountPercentage(discountController);
        setDiscountLimit(discountController);
        setUsageFrequency(discountController);
        discountController.finalizeTheNewDiscountCode();
        return discountController.getDiscount();
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
            if(input.matches("back")){
                this.parentMenu.execute();
            }
            try {
                discountController.setCustomersForDiscountCode(input);
            } catch (DiscountController.InvalidUsernameException e){
                System.err.println(e.getMessage());
            }
        }
    }
}
