package View;

import Models.Log;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;

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
            System.out.printf("%5s%10s%10d",log.getId(),log.getDate(),log.getTotalPrice());
        }
        String input;
        while(!(input = scanner.nextLine().trim()).equals("back")){
            if(input.equals("help")){
                this.help();
            }
            //TODO PASS ORDERID AND RATE TO MENU

        }
    }
}
