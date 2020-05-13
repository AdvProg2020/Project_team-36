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
        while (!((input.matches("back")) || (input.matches("help")))) {
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
        if (input.matches("back")) {
            this.parentMenu.execute();
        } else if (input.matches("help")) {
            this.help();
            this.execute();
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
                    if (chosenField.matches("back")) {
                        this.parentMenu.execute();
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
            if (choice.matches("add")) {
                addDiscountToCustomers(discount);
                return;
            } else if (choice.matches("remove")) {
                removeDiscountFromCustomers(discount);
                return;
            } else {
                System.out.println("please enter either remove or add");
            }
        }
    }

    private void addDiscountToCustomers(Discount discount) {
        System.out.println("choose the customers you want to give this discount code to and when you're done enter end :");
        String input;
        int number = 1;
        for (Customer customer : managerController.getCustomersWithoutThisCode(id)) {
            System.out.println(number + ") " + customer.getUsername());
            number++;
        }
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            try {
                managerController.setCustomersForEditingDiscountCode(input);
            } catch (ManagerController.InvalidUsernameException e) {
                System.err.println(e.getMessage());
            }
        }
        managerController.giveCodeToSelectedCustomers(discount);
    }

    private void removeDiscountFromCustomers(Discount discount) {
        System.out.println("choose the customers you want to remove this discount code from and when you're done enter end :");
        String input;
        int number = 1;
        for (Customer customer : managerController.getCustomersWithThisCode(id)) {
            System.out.println(number + ") " + customer.getUsername());
            number++;
        }
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            try {
                managerController.setCustomersForEditingDiscountCode(input);
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
}
