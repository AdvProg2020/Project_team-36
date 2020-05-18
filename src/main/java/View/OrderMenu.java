package View;

import Controllers.CustomerController;
import Controllers.ProductsController;
import Models.Customer;
import Models.CustomerLog;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class OrderMenu extends Menu {

    public OrderMenu(String name, Menu parentMenu) {
        super(name, parentMenu);

    }

    @Override
    public void help() {

        System.out.println("show order [orderId]\n " +
                "rate [productId] [1-5]\n" +
                "logout\n" +
                "sort by [field] ascending\\descending");
        System.out.println("Fields:");
        System.out.println("date\\payable\\discount amount\\gift amount");
    }

    @Override
    public void execute() {
        ArrayList<CustomerLog> logs = customerController.getAllLogs();
        printLog(logs);
        String input;
        while (!(input = scanner.nextLine().trim()).matches("back|logout")) {
            Matcher matcher;
            if (input.equals("help")) {
                this.help();
            } else if ((matcher = getMatcher(input, "show\\s+order\\s+(\\d+)")).matches()) {
                showOrderMenu(Integer.parseInt(matcher.group(1)));

            } else if ((matcher = getMatcher(input, "rate\\s+(\\d+)\\s+(\\d+)")).matches()) {
                rateProductMenu(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }else if((matcher=getMatcher(input,"sort\\s+by\\s+(.*)\\s+(.*)")).matches()){
                if(!matcher.group(2).matches("ascending|descending"))
                    System.out.println("invalid type!");
                else{
                    try {
                        printLog(customerController.sortLogs(matcher.group(1),matcher.group(2)));
                    } catch (ProductsController.NoSortException e) {
                        System.out.println("hre is no field with this name!");
                    }
                }
            } else {
                System.err.println("invalid command!");
            }


        }if(input.matches("back"))
        this.parentMenu.execute();
        else if(input.matches("logout"))
            logoutChangeMenu();
    }

    private void printLog(ArrayList<CustomerLog> logs){
        System.out.printf("%5s%20s%10s", "logId", "Date", "Total price");
        for (CustomerLog log : logs) {
            System.out.printf("%5s%20s%10d", log.getId(), log.getDate(), log.getTotalPayable());
        }
    }

    private void showOrderMenu(int orderId) {
        try {
            CustomerLog log = customerController.getOrder(orderId);
            System.out.println(log);
        } catch(CustomerController.NoLogWithId e){
            System.err.println(e.getMessage());
        }
    }

    private void rateProductMenu(int productId, int rate) {
        if(rate>5||rate<1){
            System.err.println("Unacceptable rate number! Try again");
        }
        try{
            customerController.rateProduct(productId,rate);
            System.out.println("Thank you!");
        } catch (CustomerController.NoProductWithIdInLog e) {
            System.err.println("you can only rate buying products!");
        }
    }
}
