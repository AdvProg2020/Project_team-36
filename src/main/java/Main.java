import Models.User;
import View.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User.addTest();
        Menu.setScanner(new Scanner(System.in));
        Menu.setControllers();
        Menu runMenu = new MainMenu();
        runMenu.execute();


    }
}
