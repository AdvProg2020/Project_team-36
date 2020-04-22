package View;

import Controllers.EntryController;


import java.util.regex.Matcher;

public class EntryMenu extends Menu {
    public EntryMenu(Menu parentMenu) {
        super("UserAreaMenu", parentMenu);
    }

    @Override
    public void help() {
        System.out.println("All commands:");
        System.out.println("create account [type] [username]");
        System.out.println("login [username]");
        System.out.println("NOTE: type of the account can be either manager,customer or seller");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if (input.matches("back")) {
            parentMenu.getParentMenu().execute();
            return;
        }
        if ((matcher = getMatcher(input, "create\\s+account\\s+(\\w+)\\s+(\\S+)")).matches()) {
            try {
                entryController.setUsernameRegister(matcher.group(1), matcher.group(2));
                registerProcess();
                System.out.println("register successfull");
                execute();
                return;
            } catch (EntryController.InvalidUsernameException | EntryController.ManagerExistsException | EntryController.InvalidTypeException e) {
                System.err.println(e.getMessage());
                execute();
                return;
            }
        } else if ((matcher = getMatcher(input, "login\\s+(\\S+)")).matches()) {
            try {
                entryController.setUserNameLogin(matcher.group(1));
                loginProcess();
                parentMenu.execute();
            } catch (EntryController.InvalidUsernameException e) {
                System.err.println(e.getMessage());
                execute();
                return;
            }
        } else if (input.equals("help")) {
            help();
            execute();
        } else {
            System.err.println("Invalid command! Try again please!");
            execute();
        }
    }

    private void registerProcess() {
        String input;
        System.out.print("password: ");
        entryController.setPassword(scanner.nextLine());
        System.out.print("firstname: ");
        entryController.setFirstname(scanner.nextLine());
        System.out.print("lastname: ");
        entryController.setLastname(scanner.nextLine());
        System.out.println("email");
        while (true) {
            input = scanner.nextLine().trim();
            if (input.matches("\\S+@\\S+\\.\\S+")) {
                entryController.setEmail(input);
                break;
            } else {
                System.err.println("invalid form of email!\n Correct format: abcd@abc.abc");
            }
        }
        System.out.print("phoneNumber: ");
        while (true) {
            input = scanner.nextLine();
            if (input.matches("\\d+")) {
                entryController.setPhoneNumber(input);
                break;
            } else {
                System.err.println("invalid phone number! Try again");
            }
        }
        entryController.register();

    }

    private void loginProcess() {
        System.out.print("password: ");
        while (true) {
            try {
                entryController.setPasswordLogin(scanner.nextLine(), (UserAreaMenu) parentMenu);
                System.out.println("login successfull");
                break;
            } catch (EntryController.WrongPasswordException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void execute(String username) {

    }
}
