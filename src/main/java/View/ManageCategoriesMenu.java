package View;

import Controllers.CategoryController;
import Controllers.ProductsController;
import Models.Category;
import Models.Field;
import Models.IntegerField;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ManageCategoriesMenu extends Menu {
    private String category;
    private CategoryController categoryController;

    public ManageCategoriesMenu(Menu parentMenu) {
        super("ManageCategories", parentMenu);
        subMenus.put("edit\\s+(.+)", getEditCategoryMenu());
        subMenus.put("add\\s+(.+)", getAddCategoryMenu());
        subMenus.put("remove\\s+(.+)", getRemoveCategoryMenu());
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
                getParentCategory();
                getIntegerFields();
                getOptionalFields();
            }
        };
    }

    private void getParentCategory() {
        int i = 1;
        for (Category eachCategory : categoryController.getAllCategories()) {
            System.out.println(i + "." + eachCategory.getName());
            i++;
        }
        System.out.println("enter the number of category you want to put this in,or press 0 if you dont want it to be in any category");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("back"))
                this.parentMenu.execute();
            else if (input.matches("logout"))
                logoutChangeMenu();
            else if (!input.matches("\\d+") || (input.matches("\\d+") && Integer.parseInt(input) >= i))
                System.err.println("invalid! Try again");
            else {
                categoryController.setParentCategory(Integer.parseInt(input));
                break;
            }
        }
    }

    private void getIntegerFields() {
        System.out.println("Enter the names of integer fields you want to add and then type end\nNOTE: duplicates are not count\nthis " +
                "category has its parent fields!");
        String input;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back"))
                this.parentMenu.execute();
            else if (input.equalsIgnoreCase("logout"))
                logoutChangeMenu();
            try {
                categoryController.setIntegerField(input);
            } catch (CategoryController.ThereIsFieldWithNameException e) {
                System.out.println("there is a field with this name in fields! try again or type end");
            }
        }
    }

    private void getOptionalFields() {
        System.out.println("Enter the names of optional fields you want to add and then type end to save your new category\n" +
                "NOTE: duplicates are not accepted!\n this category has its parent category fields!");
        String input;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.equalsIgnoreCase("back"))
                this.parentMenu.execute();
            else if (input.equalsIgnoreCase("logout"))
                logoutChangeMenu();
            try {
                categoryController.setOptionalField(input);
            } catch (CategoryController.ThereIsFieldWithNameException e) {
                System.out.println("there is a field with this name in fields! try again or type end");
            }
        }
        categoryController.acceptCategory();
        System.out.println("Category added successfully.Returning to Categories menu...");
    }

    private Menu getEditCategoryMenu() {
        return new Menu("EditCategoryMenu", this) {
            @Override
            public void help() {
                System.out.println("you are in editing category menu!\nlogout\nback");
            }

            @Override
            public void execute() {
                String input;
                try {
                    categoryController.editCategory(category);
                } catch (CategoryController.InvalidCategoryName e) {
                    System.err.println("There is no category with this name!Returning to category menu...");
                    return;
                }
                while (true) {
                    System.out.println("fields you can edit:\nname\nfields\nfor editing each part type it\n" +
                            "after all edits type end!");
                    input = scanner.nextLine().trim();
                    if (input.matches("back"))
                        return;
                    else if (input.matches("logout"))
                        logoutChangeMenu();
                    else if (input.matches("help"))
                        help();
                    else if (input.equalsIgnoreCase("name"))
                        editName();
                    else if (input.equalsIgnoreCase("fields"))
                        editField();
                    else
                        System.err.println("Invalid command!Try again");
                }
            }
        };
    }

    private void editName() {
        System.out.println("new name:");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("logout"))
            logoutChangeMenu();
        else if (input.equalsIgnoreCase("back"))
            this.parentMenu.execute();
        try {
            categoryController.editName(input);
        } catch (CategoryController.InvalidCategoryName invalidCategoryName) {
            System.out.println("there is a category with this name");
        }
        System.out.println("Edit completed");
    }


    private void editField() {
        System.out.println("Current fields:");
        for (Field field : categoryController.getPendableCategory().getAllFields()) {
            System.out.println(field.getName() + "  type:" + field.getClass().getSimpleName());
        }
        System.out.println("enter one of these commands:\nrename [field]\nadd [field]\nremove [field]");
        String input = scanner.nextLine().trim();
        if (input.matches("rename (\\D+)"))
            renameField(input.split(" ")[1]);
        else if (input.matches("remove (\\D+)"))
            removeField(input.split(" ")[1]);
        else if (input.matches("add (\\D+)"))
            addField(input.split(" ")[1]);

    }

    private void renameField(String name) {
        try {
            categoryController.editField(name);
        } catch (CategoryController.NoFieldWithNameException e) {
            System.out.println("there is no field with this name!");
            return;
        }
        System.out.println("enter new name:");
        try {
            categoryController.renameField(scanner.nextLine().trim());
            System.out.println("Field renamed successfully!");
        } catch (CategoryController.ThereIsFieldWithNameException e) {
            System.out.println("There is field with this name in your category!");
        } catch (CategoryController.ThereIsFieldWithNameInSubCategory e) {
            System.out.println("There is field with this name in " + e.getCategory().getName() + " category. you have to remove it first");
        }

    }

    private void removeField(String name) {
        try {
            categoryController.editField(name);
            categoryController.removeField(name);
        } catch (CategoryController.NoFieldWithNameException e) {
            System.out.println("there is no field with this name!");
            return;
        }
    }

    private void addField(String name) {
        String input;
        System.out.println("What type is this field?Enter the number:\n1.Integer field\n2.Optional field");
        System.out.println("Hint:Numerical fields can only get numbers but optional fields can have any type of data");

        while ((input = scanner.nextLine().trim()).matches("back|logout")) {
            if (input.matches("\\d+")) {
                int number = Integer.parseInt(input);
                try {
                    if (number == 1)
                        categoryController.addField(name, "IntegerField");
                    else if (number == 2)
                        categoryController.addField(name, "OptionalField");
                    else
                        System.err.println("invalid number!");
                } catch (CategoryController.ThereIsFieldWithNameException e) {
                    System.err.println("There is a field with this name in category!");
                } catch (CategoryController.ThereIsFieldWithNameInSubCategory e) {
                    System.err.println("There is a field with another type in the subCategory" + e.getCategory().getName());
                }
            } else {
                System.err.println("invalid command!");
            }
        }
        if (input.matches("back")) {
            this.parentMenu.execute();
        } else if (input.matches("logout")) {
            logoutChangeMenu();
        }
    }

    private Menu getRemoveCategoryMenu() {
        return new Menu("removeCategoryMenu", this) {
            @Override
            public void help() {}

            @Override
            public void execute() {
                try {
                    categoryController.removeCategory(category);
                } catch (ProductsController.NoCategoryWithName noCategoryWithName) {
                    System.out.println("There is no category with this name!");
                }
            }
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

    public static void printCategoryTree(Category category) {
        for (Category subCategory : category.getSubCategories()) {
            printEachCategory(subCategory, 0);
        }
    }

    private static void printEachCategory(Category category, int indent) {

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
