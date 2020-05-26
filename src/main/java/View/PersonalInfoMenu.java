package View;

import Controllers.UserController;
import Models.User;

import java.util.regex.Matcher;

public class PersonalInfoMenu extends Menu {

    public PersonalInfoMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
    }

    @Override
    public void help() {
        System.out.println("edit [field]\nlogout");
    }

    @Override
    public void execute() {
        String input;
        User user = userController.getLoggedInUser();
        System.out.println(user);
        while (!(input = scanner.nextLine().trim()).matches("back")) {
            if (input.matches("help"))
                help();
            else if (input.matches("logout"))
                logoutChangeMenu();
            else if (input.matches("edit\\s+(.+)")) {
                Matcher matcher = getMatcher(input, "edit\\s+(.+)");
                System.out.println(matcher.matches());
                matcher.matches();
                try {
                    getNewQuality(matcher.group(1));
                } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                    System.err.println("There is no field with this name!");
                }
            } else {
                System.out.println("invalid command");
            }

        }
        this.parentMenu.execute();
    }

    private void getNewQuality(String type) throws UserController.NoFieldWithThisType {
        String result;
        if (type.equalsIgnoreCase("email")) {
            while (!(result = scanner.nextLine().trim()).matches(".+@.+\\..*|back")) {
                System.out.println("wrong format!");
            }
        } else if (type.equalsIgnoreCase("phone number")) {
            while (!(result = scanner.nextLine().trim()).matches("\\d+|back")) {
                System.out.println("wrong format!");
            }
        } else
            result = scanner.nextLine().trim();
        userController.editInfo(type, result);
    }
}
