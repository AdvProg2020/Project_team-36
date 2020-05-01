package View;

import Controllers.CustomerController;
import Models.Customer;
import Models.Log;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class OrderMenu extends Menu {

    public OrderMenu(String name, Menu parentMenu) {
        super(name, parentMenu);

    }

    @Override
    public void help() {

        System.out.println("show order [orderId]\n rate [productId] [1-5]");
    }

    @Override
    public void execute() {
        ArrayList<Log> logs = customerController.getAllLogs();
        System.out.printf("%5s%10s%10s", "logId", "Date", "Total price");
        for (Log log : logs) {
            System.out.printf("%5s%10s%10d", log.getId(), log.getDate(), log.getTotalPrice());
        }
        String input;
        while (!(input = scanner.nextLine().trim()).equals("back")) {
            Matcher matcher;
            if (input.equals("help")) {
                this.help();
            } else if ((matcher = getMatcher(input, "show\\s+order\\s+(\\d+)")).matches()) {
                showOrderMenu(Integer.parseInt(matcher.group(1)));

            } else if ((matcher = getMatcher(input, "rate\\s+(\\d+)\\s+(\\d+)")).matches()) {
                rateProductMenu(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } else {
                System.err.println("invalid command!");
            }


        }
        this.parentMenu.execute();
    }

    private void showOrderMenu(int orderId) {
        try {
            Log log = customerController.getOrder(orderId);
            System.out.println(log);
        } catch(CustomerController.NoLogWithId e){
            System.err.println(e.getMessage());
        }
    }

    private void rateProductMenu(int productId, int rate) {
        if(rate>5||rate<1){
            System.err.println("Unacceptable number! Try again");
        }
        try{
            customerController.rateProduct(productId,rate);
            System.out.println("Thank you!");
        } catch (CustomerController.NoProductWithIdInLog e) {
            System.err.println(e.getMessage());
        }
    }
}
