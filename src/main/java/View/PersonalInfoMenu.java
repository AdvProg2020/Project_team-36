package View;

import Exceptions.NoLoggedInSellerException;
import Exceptions.NoLoggedInUserException;
import Models.Seller;

import java.util.HashMap;

public class PersonalInfoMenu extends Menu{
    private String type;
    public PersonalInfoMenu( Menu parentMenu) {
        super("PersonalInfoMenu", parentMenu);
        subMenus = new HashMap<>();
        subMenus.put("edit\\s+first\\s+name",getEditFirstNameMenu());
        subMenus.put("edit\\s+last\\s+name",getEditLastNameMenu());
        subMenus.put("edit\\s+email",getEditEmailMenu());
        subMenus.put("edit\\s+phone\\s+number",getEditPhoneNumberMenu());
        subMenus.put("edit\\s+password",getEditPasswordMenu());
        try {
            type = userController.getType();
        }catch (NoLoggedInUserException e){
            System.err.println(e.getMessage());
        }
        if (!type.equals("manager")){
            subMenus.put("edit\\s+credit",getEditCreditMenu());
        }
        if (type.equals("seller")){
            subMenus.put("edit\\s+company\\s+name",getEditCompanyNameMenu());
            subMenus.put("edit\\s+company\\s+info",getEditCompanyInfoMenu());
        }
    }

    public Menu getEditFirstNameMenu(){
        return new Menu("edit\\s+first\\s+name",this) {
            @Override
            public void execute(){
                try {
                    String newFirstName = scanner.next();
                    userController.setFirstname(newFirstName);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void help() {
            }
        };
    }

    public Menu getEditLastNameMenu(){
        return new Menu("edit\\s+last\\s+name",this) {
            @Override
            public void execute(){
                try {
                    String newLastName = scanner.next();
                    userController.setLastname(newLastName);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void help() {
            }
        };
    }

    public Menu getEditEmailMenu(){
        return new Menu("edit\\s+email",this) {
            @Override
            public void execute(){
                try {
                    String newEmail = scanner.next();
                    userController.setEmail(newEmail);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void help() {
            }
        };
    }

    public Menu getEditPhoneNumberMenu(){
        return new Menu("edit\\s+phone\\s+number",this) {
            @Override
            public void execute(){
                try {
                    String newPhoneNumber = scanner.next();
                    userController.setPhoneNumber(newPhoneNumber);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void help() {
            }
        };
    }

    public Menu getEditPasswordMenu(){
        return new Menu("edit\\s+password",this) {
            @Override
            public void execute(){
                try {
                    String newPassword = scanner.next();
                    userController.setPassword(newPassword);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void help() {
            }
        };
    }

    public Menu getEditCreditMenu(){
        return new Menu("edit\\s+credit",this) {
            @Override
            public void execute() {
                if (type.equals("seller")){
                   try {
                       long newCredit = scanner.nextLong();
                       sellerController.setCredit(newCredit);
                   }catch (NoLoggedInUserException e){
                       System.err.println(e.getMessage());
                   }
                }else {
                    try {
                        long newCredit = scanner.nextLong();
                        customerController.setCredit(newCredit);
                    }catch (NoLoggedInUserException e){
                        System.err.println(e.getMessage());
                    }
                }
            }

            @Override
            public void help() {

            }
        };
    }

    public Menu getEditCompanyNameMenu(){
        return new Menu("edit\\s+company\\s+name",this) {
            @Override
            public void execute() {
                try {
                    String newCompanyName = scanner.next();
                    sellerController.setCompanyName(newCompanyName);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }

            @Override
            public void help() {

            }
        };
    }

    public Menu getEditCompanyInfoMenu(){
        return new Menu("edit\\s+company\\s+info",this) {
            @Override
            public void execute() {
                try {
                    String newCompanyInfo = scanner.next();
                    sellerController.setCompanyInfo(newCompanyInfo);
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }

            @Override
            public void help() {

            }
        };
    }

    @Override
    public void execute() {
        try {
            String output = "";
            output += userController.getPersonalInfo();
            System.out.println(output);
        }catch (NoLoggedInUserException e){
            System.err.println(e.getMessage());
        }

        super.execute();
    }

    @Override
    public void help() {
        System.out.println("edit [field]");
    }
}
