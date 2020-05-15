package View;

import Controllers.ManagerController;
import Controllers.NewManagerController;
import Models.User;
import java.util.regex.Matcher;

public class ManageUsersMenu extends Menu {

    private String username;

    public ManageUsersMenu(Menu parentMenu) {
        super("ManageUsersMenu", parentMenu);
        subMenus.put("view\\s+(\\S+)",viewUser());
        subMenus.put("delete\\s+user\\s+(\\S+)",deleteUser());
        subMenus.put("create\\s+manager\\s+profile",createManagerProfile());
    }


    @Override
    public void help() {
        System.out.println("view [username]\n" +
                "delete user [username]\n" +
                "create manager profile\n");
    }

    @Override
    public void execute() {
        int number =1;
        for (User user : managerController.getAllUsers()) {
            System.out.println(number+") "+user.getUsername());
            number+=1;
        }
        Matcher matcher;
        Menu chosenMenu = null;
        System.out.println("choose the user account and what you want to do with it :");
        String input = scanner.nextLine().trim();
        while (!((input.matches("back")) || (input.matches("help")))) {
            for (String regex : subMenus.keySet()) {
                matcher = getMatcher(input, regex);
                if (matcher.matches()) {
                    chosenMenu = subMenus.get(regex);
                    getInput(input,matcher);
                    break;
                }
            }
            if (chosenMenu == null) {
                System.err.println("Invalid command! Try again please");
            } else {
                chosenMenu.execute();
            }
            chosenMenu=null;
            input = scanner.nextLine().trim();
        }
        if (input.matches("back")) {
            this.parentMenu.execute();
        } else if (input.matches("help")) {
            this.help();
            this.execute();
        }
    }

    private void getInput(String input,Matcher matcher){
        if(input.startsWith("view") || input.startsWith("delete")){
            username = matcher.group(1);
        }
    }

    private Menu viewUser() {
        return new Menu("viewUser", this) {
            @Override
            public void help() {
                System.out.println("view user");
            }

            @Override
            public void execute() {
                try {
                    User user = managerController.getUserWithUsername(username);
                    System.out.println(user);
                } catch (ManagerController.InvalidUsernameException e){
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    private Menu deleteUser() {
        return new Menu("deleteUser", this) {
            @Override
            public void help() {
                System.out.println("delete user");
            }

            @Override
            public void execute() {
                try {
                    User user = managerController.getUserWithUsername(username);
                    managerController.deleteUser(user);
                } catch (ManagerController.InvalidUsernameException e){
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    private Menu createManagerProfile() {
        return new Menu("createManagerProfile", this) {
            @Override
            public void help() {
                System.out.println("create a new manager profile");
            }

            @Override
            public void execute() {
                NewManagerController newManagerController = new NewManagerController();
                System.out.println("please fill each field wanted :");
                System.out.println("username: ");
                while (true){
                    try {
                        newManagerController.setUsername(scanner.nextLine());
                        break;
                    } catch (NewManagerController.InvalidInputException e){
                        System.err.println(e.getMessage());
                    }
                }
                System.out.print("password: ");
                newManagerController.setPassword(scanner.nextLine());
                System.out.print("firstname: ");
                newManagerController.setFirstname(scanner.nextLine());
                System.out.print("lastname: ");
                newManagerController.setLastname(scanner.nextLine());
                System.out.println("email: ");
                while (true) {
                    try {
                        newManagerController.setEmail(scanner.nextLine());
                        break;
                    } catch (NewManagerController.InvalidInputException e){
                        System.err.println(e.getMessage());
                    }
                }
                System.out.print("phoneNumber: ");
                while (true) {
                    try {
                        newManagerController.setPhoneNumber(scanner.nextLine());
                        break;
                    } catch (NewManagerController.InvalidInputException e){
                        System.err.println(e.getMessage());
                    }
                }
                newManagerController.finalizeMakingNewManagerProfile();
                System.out.println("a new manager profile was made successfully");
            }
        };
    }

    //-..-
}
