import View.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu.setScanner(new Scanner(System.in));
        Menu.setControllers();
        Menu runMenu = new MainMenu();
        runMenu.execute();
    }
}
