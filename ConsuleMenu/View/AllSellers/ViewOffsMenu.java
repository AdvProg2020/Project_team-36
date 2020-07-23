package View.AllSellers;

import Controllers.NewOffController;
import Controllers.SellerController;
import Models.*;
import View.Menu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

public class ViewOffsMenu extends Menu {

    private int id;

    public ViewOffsMenu(Menu parentMenu) {
        super("ViewOffsMenu", parentMenu);
        subMenus.put("^view\\s+(\\d+)$",getViewOff());
        subMenus.put("^add\\s+off",getAddOff());
        subMenus.put("^edit\\s+(\\d+)$",getEditOff());
    }

    @Override
    public void help() {
        System.out.println("view [offId]\n" +
                "edit [offId]\n" +
                "add off\n");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Sale off : sellerController.getAllSellerSales()) {
            System.out.println(number + ") " + off.getOffId() + "  " + (off.getSalePercent()*100)+"%");
            number += 1;
        }
        System.out.println("choose the off and what you want to do with it :");
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
            number=1;
            for (Sale off : sellerController.getAllSellerSales()) {
                System.out.println(number + ") " + off.getOffId() + "  " + (off.getSalePercent()*100)+"%");
                number += 1;
            }
            System.out.println("choose the off and what you want to do with it :");
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

    private Menu getViewOff(){
        return new Menu("getViewOff",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    Sale off = sellerController.getSaleWithId(id);
                    System.out.println(off);
                } catch (SellerController.InvalidOffIdException e){
                    System.err.println("you don't have an off with these id");
                }
            }
        };
    }

    private Menu getAddOff(){
        return new Menu("getAddOff",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                NewOffController newOff = new NewOffController(sellerController);
                System.out.println("please enter the required information:");
                setStartDateForOff(newOff);
                setEndDateForOff(newOff);
                setOffPercentage(newOff);
                setProductsIncludedForOff(newOff);
                newOff.sendNewOffRequest();
            }
        };
    }

    private Menu getEditOff(){
        return new Menu("getEditOff",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    Sale selectedOff = sellerController.getSaleWithId(id);
                    Sale off = sellerController.getOffCopy(selectedOff);
                    System.out.println(off);
                    System.out.println("choose the field you want to edit using these commands :\n" +
                            "start date\n" +
                            "end date\n" +
                            "off percent\n" +
                            "products included"
                    );
                    String chosenField = scanner.nextLine().trim();
                    if (chosenField.equalsIgnoreCase("back")) {
                        this.parentMenu.execute();
                    } else if (chosenField.equalsIgnoreCase("logout")){
                        logoutChangeMenu();
                    }
                    if (chosenField.matches("products\\s+included")) {
                        editProductsIncluded(off);
                        System.out.println("edit was done successfully");
                        return;
                    }
                    Method editor = sellerController.getOffFieldEditor(chosenField, sellerController);
                    System.out.println("enter your desired new value :");
                    while (true) {
                        try {
                            String newValue = scanner.nextLine().trim();
                            if (newValue.equalsIgnoreCase("back")) {
                                this.parentMenu.execute();
                            } else if (newValue.equalsIgnoreCase("logout")){
                                logoutChangeMenu();
                            }
                            sellerController.invokeOffEditor(newValue, off, editor);
                            sellerController.sendEditOffRequest(off);
                            System.out.println("edit request was sent successfully");
                            this.execute();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            if(e.getCause() instanceof SellerController.StartDateAfterEndDateException){
                                System.err.println("start date must be before the end date");
                            } else if (e.getCause() instanceof SellerController.EndDateBeforeStartDateException){
                                System.err.println("end date must be after start date");
                            } else if (e.getCause() instanceof SellerController.InvalidDateFormatException){
                                System.err.println("date should be in the yyyy/MM/dd format");
                            } else if(e.getCause() instanceof SellerController.InvalidRangeException){
                                System.err.println("number not in the desired range");
                            } else if (e.getCause() instanceof NumberFormatException){
                                System.err.println("you can't enter anything but number");
                            }
                        }
                    }
                } catch (SellerController.InvalidOffIdException e){
                    System.err.println("you don't have an off with these id");
                } catch (NoSuchMethodException wrongCommand) {
                    System.err.println("you can only edit the fields above, and also please enter the required command.");
                    this.execute();
                }
            }

        };
    }

    private void setStartDateForOff(NewOffController newOff) {
        String input;
        while (true) {
            System.out.println("start date in yyyy/MM/dd format :");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")) {
                logoutChangeMenu();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date startDate;
            try {
                startDate = dateFormat.parse(input);
                newOff.setStartTime(startDate);
                return;
            } catch (ParseException e) {
                System.err.println("input isn't in the yyyy/MM/dd format");
            }
        }
    }


    private void setEndDateForOff(NewOffController newOff) {
        String input;
        while (true) {
            System.out.println("end date in yyyy/MM/dd format:");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")) {
                logoutChangeMenu();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date endDate;
            try {
                endDate = dateFormat.parse(input);
                try {
                    newOff.setEndTime(endDate);
                    return;
                } catch (NewOffController.EndDatePassedException dateError) {
                    System.err.println("we already passed this date");
                } catch (NewOffController.EndDateBeforeStartDateException dateError){
                    System.err.println("end date must be after start date.");
                }
            } catch (ParseException formatError) {
                System.err.println("input isn't in the yyyy/MM/dd format");
            }
        }
    }

    private void setOffPercentage(NewOffController newOff) {
        int percentage;
        String input;
        while (true) {
            System.out.println("off percentage between 0-100 :");
            try {
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                } else if (input.equalsIgnoreCase("logout")) {
                    logoutChangeMenu();
                }
                percentage = Integer.parseInt(input);
                if (percentage < 100 && percentage > 0) {
                    newOff.setSalePercent(percentage * 0.01);
                    return;
                } else {
                    System.err.println("number not in the desired range");
                }
            } catch (NumberFormatException e) {
                System.err.println("you can't enter anything but number");
            }
        }
    }

    private void setProductsIncludedForOff(NewOffController newOff) {
        System.out.println("choose the products you want to put in this off and when you are done enter end :");
        String input;
        int number = 1;
        for (Product product : sellerController.getSellerProducts()) {
            System.out.println(number + ") " + product.getProductId() + "   " + product.getName());
            number++;
        }
        while (!(input = scanner.nextLine()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")) {
                logoutChangeMenu();
            }
            try {
                int productId = Integer.parseInt(input);
                newOff.setProductsInSale(productId);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            } catch (NewOffController.InvalidProductIdException e){
                System.err.println("you don'y have any product with this id");
            }catch (NewOffController.ProductAlreadyAddedException e){
                System.err.println("you have already selected this product");
            }
        }
    }

    private void editProductsIncluded(Sale off) {
        System.out.println("do you want to add products or remove them?[add\\remove]");
        while (true) {
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (choice.equalsIgnoreCase("logout")){
                logoutChangeMenu();
            }
            if (choice.equalsIgnoreCase("add")) {
                addProductsToOff(off);
                return;
            } else if (choice.equalsIgnoreCase("remove")) {
                removeProductsFromOff(off);
                return;
            } else {
                System.out.println("please enter either remove or add");
            }
        }
    }

    private void addProductsToOff(Sale off) {
        System.out.println("choose the products you want to add to this off with (add [productId]) and when you're done enter end :");
        String input;
        int number = 1;
        for (Product product : sellerController.getProductsNotInOff(off)) {
            System.out.println(number + ") " + product.getProductId()+"   "+product.getName());
            number++;
        }
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")){
                logoutChangeMenu();
            }
            try {
                if(input.matches("^add\\s+(\\d+)$")){
                    Matcher matcher = getMatcher(input,"^add\\s+(\\d+)$");
                    sellerController.setProductsToBeAddedToOff(Integer.parseInt(matcher.group(1)),off);
                } else {
                    System.err.println("wrong command. please try again.");
                }
            } catch (SellerController.ProductAlreadyAddedException e) {
                System.err.println("you've already selected this product");
            } catch (SellerController.InvalidProductIdException e) {
                System.err.println("you don't have any product with this id");
            }
        }
        sellerController.addProductsToOff(off);
    }

    private void removeProductsFromOff(Sale off) {
        System.out.println("choose the products you want to remove to this off with (remove [productId]) and when you're done enter end :");
        String input;
        int number = 1;
        for (Product product : sellerController.getProductsInOff(off)) {
            System.out.println(number + ") " + product.getProductId()+"   "+product.getName());
            number++;
        }
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")){
                logoutChangeMenu();
            }
            try {
                if(input.matches("^remove\\s+(\\d+)$")){
                    Matcher matcher = getMatcher(input,"^remove\\s+(\\d+)$");
                    sellerController.setProductsToBeRemovedFromOff(Integer.parseInt(matcher.group(1)),off);
                } else {
                    System.err.println("wrong command. please try again.");
                }
            } catch (SellerController.ProductAlreadyAddedException e) {
                System.err.println("you've already selected this product");
            } catch (SellerController.InvalidProductIdException e) {
                System.err.println("you don't have any product with this id");
            }
        }
        sellerController.removeProductsFromOff(off);
    }
}
