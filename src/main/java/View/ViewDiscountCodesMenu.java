package View;

import Controllers.DiscountController;
import Controllers.ManagerController;
import Models.Discount;

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
            }

            public void execute() {
                try {
                    Discount discount = managerController.getDiscountWithId(id);
                    System.out.println(discount);
                    parentMenu.execute();
                } catch (ManagerController.InvalidDiscountIdException invalidIdError) {
                    System.err.println(invalidIdError.getMessage());
                    parentMenu.execute();
                }
            }
        };
    }

    private Menu editDiscountCode() {
        return new Menu("editDiscountCode", this) {
            @Override
            public void help() {
            }


            public void execute() {

            }
        };
    }

    private Menu removeDiscountCode() {
        return new Menu("removeDiscountCode", this) {
            @Override
            public void help() {
            }


            public void execute() {

            }
        };
    }
}
