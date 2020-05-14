package View;

import Controllers.EntryController;


import java.util.regex.Matcher;

public class EntryMenu extends Menu {
    public EntryMenu(Menu parentMenu) {
        super("EntryMenu", parentMenu);
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
            parentMenu.execute();
        }
        else if ((matcher = getMatcher(input, "create\\s+account\\s+(\\w+)\\s+(\\S+)")).matches()) {
            try {
                entryController.setUsernameRegister(matcher.group(1), matcher.group(2));
                registerProcess(matcher.group(1));
                System.out.println("register successful");
                execute();
            } catch (EntryController.InvalidUsernameException | EntryController.ManagerExistsException | EntryController.InvalidTypeException e) {
                System.err.println(e.getMessage());
                execute();
            }
        } else if ((matcher = getMatcher(input, "login\\s+(\\S+)")).matches()) {
            try {
                entryController.setUserNameLogin(matcher.group(1));
                loginProcess();
                parentMenu.execute();
            } catch (EntryController.InvalidUsernameException e) {
                System.err.println(e.getMessage());
                execute();
            }
        } else if (input.equals("help")) {
            help();
            execute();
        } else {
            System.err.println("Invalid command! Try again please!");
            execute();
        }
    }

    private void registerProcess(String type) {
        String input;
        System.out.print("password: ");
        entryController.setPassword(scanner.nextLine().trim());
        System.out.print("firstname: ");
        entryController.setFirstname(scanner.nextLine().trim());
        System.out.print("lastname: ");
        entryController.setLastname(scanner.nextLine().trim());
        if(type.equalsIgnoreCase("seller")){
            System.out.println("company: ");
            entryController.setCompany(scanner.nextLine().trim());
            System.out.println("company information: ");
            entryController.setCompanyInfo(scanner.nextLine().trim());
        }
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
            input = scanner.nextLine().trim();
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
                entryController.setPasswordLogin(scanner.nextLine().trim(), (UserAreaMenu) parentMenu);
                System.out.println("login successful");
                break;
            } catch (EntryController.WrongPasswordException e) {
                System.err.println("Wrong password!");
            }
        }
    }

}
