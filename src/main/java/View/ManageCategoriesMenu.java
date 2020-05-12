package View;

import Controllers.CategoryController;
import Models.Category;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ManageCategoriesMenu extends Menu {
    private String category;
    private CategoryController categoryController;

    public ManageCategoriesMenu(Menu parentMenu) {
        super("ManageCategories", parentMenu);
        //subMenus.put("edit\\s+(\\D+)", getEditCategoryMenu());
        subMenus.put("add\\s+(\\D+)", getAddCategoryMenu());
        //subMenus.put("remove\\s+(\\D+)", getRemoveCategoryMenu());
    }

    @Override
    public void help() {
        System.out.println("edit [category]\n" +
                "add [category]\n" +
                "remove [category]\n" +
                "help\n" +
                "logout");
    }

    private Menu getAddCategoryMenu() {
        return new Menu("AddCategory", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    categoryController.setCategoriesName(category);
                } catch (CategoryController.InvalidCategoryName e) {
                    System.err.println("there is a category with this name!");
                    return;
                }
                try {
                    getParentCategory();
                    getIntegerFields();
                    getOptionalFields();
                } catch (BackIsPressed e) {
                    return;
                }catch (LogoutIsPressesException a){
                    logoutChangeMenu();
                }



            }

            private void getParentCategory() throws BackIsPressed, LogoutIsPressesException {
                int i = 1;
                for (Category eachCategory : categoryController.getAllCategories()) {
                    System.out.println(i + "." + eachCategory.getName());
                    i++;
                }
                System.out.println("enter the number of category you want to put this in,or press 0 if you dont want it to be in any category");
                while (true) {
                    String input = scanner.nextLine().trim();
                    if (input.matches("back"))
                        throw new BackIsPressed();
                    else if (input.matches("logout"))
                        throw new LogoutIsPressesException();
                    else if (!input.matches("\\d+") || (input.matches("\\d+") && Integer.parseInt(input) >= i))
                        System.err.println("invalid! Try again");
                    else {
                        categoryController.setParentCategory(Integer.parseInt(input));
                        break;
                    }
                }
            }

            private void getIntegerFields() throws BackIsPressed, LogoutIsPressesException {
                System.out.println("Enter the names of integer fields you want to add and then type end\nNOTE: duplicates are not count\nthis " +
                        "category has its parent fields!");
                String input;
                while(!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")){
                    if(input.equalsIgnoreCase("back"))
                        throw new BackIsPressed();
                    else if(input.equalsIgnoreCase("logout"))
                        throw new LogoutIsPressesException();
                    try{
                        categoryController.setIntegerField(input);
                    }catch(CategoryController.ThereIsFieldWithNameException e){
                        System.out.println("there is a field with this name in fields! try again or type end");
                    }
                }

            }

            private void getOptionalFields() throws BackIsPressed, LogoutIsPressesException {
                System.out.println("Enter the names of optional fields you want to add and then type end to save your new category\n" +
                        "NOTE: duplicates are not accepted!\n this category has its parent category fields!");
                String input;
                while(!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")){
                    if(input.equalsIgnoreCase("back"))
                        throw new BackIsPressed();
                    else if(input.equalsIgnoreCase("logout"))
                        throw new LogoutIsPressesException();
                    try{
                        categoryController.setOptionalField(input);
                    }catch(CategoryController.ThereIsFieldWithNameException e){
                        System.out.println("there is a field with this name in fields! try again or type end");
                    }
                }
                categoryController.acceptCategory();
                System.out.println("Category added successfully.Returning to Categories menu...");
            }

        };


    }

    private Menu getEditCategoryMenu() {
        return new Menu("EditCategoryMenu", this) {
            @Override
            public void help() {

            }
        };
    }

    private Menu getRemoveCategoryMenu() {
        return new Menu("removeCategoryMenu", this) {
            @Override
            public void help() {}
        };
    }

    @Override
    public void execute() {
        this.categoryController = new CategoryController();
        System.out.println("All Categories:");
        printCategoryTree(categoryController.getMainCategory());
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("help")) {
                help();
                this.execute();
                break;
            } else if (input.matches("back")) {
                this.parentMenu.execute();
            } else if (input.matches("logout")) {
                logoutChangeMenu();
            } else {
                Menu chosenMenu = null;
                for (String regex : subMenus.keySet()) {
                    Matcher matcher = getMatcher(input, regex);
                    if (matcher.matches()) {
                        this.category = matcher.group(1);
                        chosenMenu = subMenus.get(regex);
                    }
                }
                if (chosenMenu == null)
                    System.err.println("invalid command! Try again please");
                else {
                    chosenMenu.execute();
                    this.execute();
                }
            }
        }
    }

    private void printCategoryTree(Category category) {
        for (Category subCategory : category.getSubCategories()) {
            printEachCategory(subCategory, 0);
        }
    }

    private void printEachCategory(Category category, int indent) {

        ArrayList<Category> temp = category.getSubCategories();
        for (int i = 0; i < 3 * indent; i++)
            System.out.print(" ");
        System.out.print("*");
        if (temp.isEmpty()) {
            System.out.println(category.getName());

        } else {
            System.out.println(category.getName());
            for (Category category1 : temp) {
                printEachCategory(category1, indent + 1);
            }
        }
    }


}
