package View;

import Controllers.ManagerController;
import Models.Customer;
import Models.Discount;
import java.lang.reflect.Method;
import java.util.regex.Matcher;


public class ViewDiscountCodesMenu extends Menu {

    private int id;

    public ViewDiscountCodesMenu(Menu parentMenu) {
        super("ViewDiscountCodesMenu", parentMenu);
        subMenus.put("view\\s+discount\\s+code\\s+(\\d+)", viewDiscountCode());
        subMenus.put("edit\\s+discount\\s+code\\s+(\\d+)", editDiscountCode());
        subMenus.put("remove\\s+discount\\s+code\\s+(\\d+)", removeDiscountCode());
    }

    @Override
    public void help() {
        System.out.println("you can view edit and remove discount codes with the following commands :\n" +
                "view discount code [code]\n" +
                "edit discount code [code]\n" +
                "remove discount code [code]\n");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Discount discountCode : managerController.getAllDiscountCodes()) {
            System.out.println(number + ") " + discountCode.getId());
            number += 1;
        }
        System.out.println("choose the discount and what you want to do with it :");
        String input = scanner.nextLine().trim();
        while (!((input.equalsIgnoreCase("back"))||(input.equalsIgnoreCase("help"))||
                (input.equalsIgnoreCase("logout")))) {
            for (String regex : subMenus.keySet()) {
                matcher = getMatcher(input, regex);
                if (matcher.matches()) {
                    chosenMenu = subMenus.get(regex);
                    try {
                        this.id = Integer.parseInt(matcher.group(1));
                    } catch (NumberFormatException e) {
                        System.err.println("you can't enter anything but number as id");
                    }
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
        if (input.equalsIgnoreCase("back")) {
            this.parentMenu.execute();
        } else if (input.equalsIgnoreCase("help")) {
            this.help();
            this.execute();
        } else if(input.equalsIgnoreCase("logout")){
            logoutChangeMenu();
        }
    }

    private Menu viewDiscountCode() {
        return new Menu("viewDiscountCode", this) {
            @Override
            public void help() {
                System.out.println("view discount code");
            }

            public void execute() {
                try {
                    Discount discount = managerController.getDiscountWithId(id);
                    System.out.println(discount);
                } catch (ManagerController.InvalidDiscountIdException invalidIdError) {
                    System.err.println(invalidIdError.getMessage());
                }
            }
        };
    }

    private Menu editDiscountCode() {
        return new Menu("editDiscountCode", this) {
            @Override
            public void help() {
                System.out.println("edit discount code");
            }

            public void execute() {
                try {
                    Discount discount = managerController.getDiscountWithId(id);
                    System.out.println(discount);
                    System.out.println("choose the field you want to edit using these commands :\n" +
                            "start time\n" +
                            "termination time\n" +
                            "discount percent\n" +
                            "discount limit\n" +
                            "usage frequency\n" +
                            "customers included"
                    );
                    String chosenField = scanner.nextLine().trim();
                    if (chosenField.equalsIgnoreCase("back")) {
                        this.parentMenu.execute();
                    } else if (chosenField.equalsIgnoreCase("logout")){
                        logoutChangeMenu();
                    }
                    if (chosenField.matches("customers\\s+included")) {
                        editCustomersIncluded(discount);
                        System.out.println("edit was done successfully");
                        return;
                    }
                    Method editor = managerController.getFieldEditor(chosenField, managerController);
                    System.out.println("enter your desired new value :");
                    while (true) {
                        try {
                            String newValue = scanner.nextLine().trim();
                            if (newValue.equalsIgnoreCase("back")) {
                                this.parentMenu.execute();
                            } else if (newValue.equalsIgnoreCase("logout")){
                                logoutChangeMenu();
                            }
                            managerController.invokeEditor(newValue, discount, editor);
                            System.out.println("edit was done successfully");
                            return;
                        } catch (Exception e) {
                            System.err.println(e.getCause().getMessage());
                        }
                    }
                } catch (ManagerController.InvalidDiscountIdException invalidIdError) {
                    System.err.println(invalidIdError.getMessage());
                } catch (NoSuchMethodException wrongCommand) {
                    System.err.println("you can only edit the fields above, and also please enter the required command.");
                    this.execute();
                } catch (Exception e) {
                    System.err.println(e.getCause().getMessage());
                    this.execute();
                }
            }
        };
    }

    private void editCustomersIncluded(Discount discount) {
        System.out.println("do you want to add customers or remove them?[add\\remove]");
        while (true) {
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (choice.equalsIgnoreCase("logout")){
                logoutChangeMenu();
            }
            if (choice.equalsIgnoreCase("add")) {
                addDiscountToCustomers(discount);
                return;
            } else if (choice.equalsIgnoreCase("remove")) {
                removeDiscountFromCustomers(discount);
                return;
            } else {
                System.out.println("please enter either remove or add");
            }
        }
    }

    private void addDiscountToCustomers(Discount discount) {
        System.out.println("choose the customers you want to give this discount code to with (add [username]) and when you're done enter end :");
        String input;
        int number = 1;
        for (Customer customer : managerController.getCustomersWithoutThisCode(id)) {
            System.out.println(number + ") " + customer.getUsername());
            number++;
        }
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")){
                logoutChangeMenu();
            }
            try {
                if(input.matches("remove\\s+(\\S+)")){
                    Matcher matcher = getMatcher(input,"remove\\s+(\\S+)");
                    managerController.setCustomersForEditingDiscountCode(matcher.group(1));
                } else {
                    System.err.println("wrong command. please try again.");
                }
            } catch (ManagerController.InvalidUsernameException e) {
                System.err.println(e.getMessage());
            }
        }
        managerController.giveCodeToSelectedCustomers(discount);
    }

    private void removeDiscountFromCustomers(Discount discount) {
        System.out.println("choose the customers you want to remove this discount code from with (remove [username]) and when you're done enter end :");
        String input;
        int number = 1;
        for (Customer customer : managerController.getCustomersWithThisCode(id)) {
            System.out.println(number + ") " + customer.getUsername());
            number++;
        }
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")){
                logoutChangeMenu();
            }
            try {
                if(input.matches("remove\\s+(\\S+)")){
                    Matcher matcher = getMatcher(input,"remove\\s+(\\S+)");
                    managerController.setCustomersForEditingDiscountCode(matcher.group(1));
                } else {
                    System.err.println("wrong command. please try again.");
                }
            } catch (ManagerController.InvalidUsernameException e) {
                System.err.println(e.getMessage());
            }
        }
        managerController.removeCodeFromSelectedCustomers(discount);
    }

    private Menu removeDiscountCode() {
        return new Menu("removeDiscountCode", this) {
            @Override
            public void help() {
                System.out.println("remove discount code");
            }

            public void execute() {
                try {
                    managerController.removeDiscount(id);
                    System.out.println("discount code was removed successfully");
                } catch (ManagerController.InvalidDiscountIdException invalidIdError) {
                    System.err.println(invalidIdError.getMessage());
                }
            }
        };
    }

    //-..-
}
