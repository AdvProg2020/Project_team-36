package View.AllSellers;

import Controllers.EditProductController;
import Controllers.NewProductController;
import Controllers.SellerController;
import Models.*;
import View.ManageCategoriesMenu;
import View.Menu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

public class ManageProductsMenu extends Menu {

    private int id;

    public ManageProductsMenu(Menu parentMenu) {
        super("ManageProductsMenu", parentMenu);
        subMenus.put("^view\\s+(\\d+)$", getViewProduct());
        subMenus.put("^view\\s+buyers\\s+(\\d+)$", getViewBuyers());
        subMenus.put("^edit\\s+(\\d+)$", getEditProduct());
        subMenus.put("^remove\\s+product\\s+(\\d+)$", getRemoveProduct());
    }

    @Override
    public void help() {
        System.out.println("view [productId]\n" +
                "view products [productId]\n" +
                "edit [product]\n" +
                "remove product [productId]\n");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Product product : sellerController.getSellerProducts()) {
            System.out.println(number + ") " + product.getProductId() + " " + product.getName());
            number += 1;
        }
        System.out.println("choose the product and what you want to do with it :");
        String input = scanner.nextLine().trim();
        while (!((input.equalsIgnoreCase("back")) || (input.equalsIgnoreCase("help")) ||
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
            chosenMenu = null;
            input = scanner.nextLine().trim();
        }
        if (input.equalsIgnoreCase("back")) {
            this.parentMenu.execute();
        } else if (input.equalsIgnoreCase("help")) {
            this.help();
            this.execute();
        } else if (input.equalsIgnoreCase("logout")) {
            logoutChangeMenu();
        }
    }

    private Menu getViewProduct() {
        return new Menu("getViewProduct", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    Product product = sellerController.getSellerProductWithId(id);
                    System.out.println(sellerController.getSellerProductDetail(product));
                } catch (SellerController.NoProductForSeller e) {
                    System.err.println("There is no product with this id in your products!");
                }
            }
        };
    }

    private Menu getViewBuyers() {
        return new Menu("getViewBuyers", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    Product product = sellerController.getSellerProductWithId(id);
                    int number = 1;
                    for (Customer buyer : sellerController.getAllBuyers(product)) {
                        System.out.println(number + ") " + buyer.getUsername());
                        number += 1;
                    }
                } catch (SellerController.NoProductForSeller e) {
                    System.err.println("There is no product with this id in your products!");
                }
            }
        };
    }

    private Menu getEditProduct() {
        return new Menu("getEditProduct", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    EditProductController editProductController = new EditProductController(sellerController.getLoggedInSeller());
                    Product selectedProduct = sellerController.getSellerProductWithId(id);
                    Product product = editProductController.getProductCopy(selectedProduct);
                    System.out.println(product);
                    System.out.println("choose the field you want to edit using these commands :\n" +
                            "name\n" +
                            "company\n" +
                            "category\n" +
                            "fields of category\n" +
                            "information\n" +
                            "price\n" +
                            "supply"
                    );
                    String chosenField = scanner.nextLine().trim();
                    if (chosenField.equalsIgnoreCase("back")) {
                        this.parentMenu.execute();
                    } else if (chosenField.equalsIgnoreCase("logout")) {
                        logoutChangeMenu();
                    }
                    if (chosenField.matches("^category$")) {
                        editProductCategory(product, editProductController);
                        editProductController.sendEditProductRequest(product);
                        System.out.println("edit was done successfully");
                        return;
                    } else if (chosenField.matches("^fields\\s+of\\s+category$")) {
                        editProductCategoryField(product,editProductController);
                        editProductController.sendEditProductRequest(product);
                        System.out.println("edit was done successfully");
                        return;
                    }
                    Method editor = editProductController.getProductFieldEditor(chosenField, sellerController);
                    System.out.println("enter your desired new value :");
                    while (true) {
                        try {
                            String newValue = scanner.nextLine().trim();
                            if (newValue.equalsIgnoreCase("back")) {
                                this.parentMenu.execute();
                            } else if (newValue.equalsIgnoreCase("logout")) {
                                logoutChangeMenu();
                            }
                            editProductController.invokeProductEditor(newValue, product, editor);
                            if(chosenField.equalsIgnoreCase("price")||chosenField.equalsIgnoreCase("supply")){
                                editProductController.sendEditProductFieldRequest(product);
                            }else {
                                editProductController.sendEditProductRequest(product);
                            }
                            System.out.println("edit request was sent successfully");
                            return;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            if (e.getCause() instanceof NumberFormatException) {
                                System.err.println("you can't enter anything but number");
                            }
                        }
                    }
                } catch (SellerController.NoProductForSeller e) {
                    System.err.println("you don't have a product with these id");
                } catch (NoSuchMethodException wrongCommand) {
                    System.err.println("you can only edit the fields above, and also please enter the required command.");
                    this.execute();
                }
            }
        };
    }

    public Menu getRemoveProduct() {
        return new Menu("getRemoveProduct", this) {
            @Override
            public void execute() {
                try {
                    sellerController.removeSellerProduct(id);
                    System.out.println("Product removed!");
                } catch (SellerController.NoProductForSeller noProductForSeller) {
                    System.err.println("There is no product with this id in your products!");
                }
            }

            @Override
            public void help() {
            }
        };
    }

    private void editProductCategory(Product product, EditProductController editProductController) {
        System.out.println("All Categories:");
        ManageCategoriesMenu.printCategoryTree(sellerController.getMainCategory());
        System.out.println("choose your desired new category: ");
        while (true) {
            try {
                String newValue = scanner.nextLine().trim();
                if (newValue.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                } else if (newValue.equalsIgnoreCase("logout")) {
                    logoutChangeMenu();
                }
                editProductController.editProductCategory(newValue, product);
                System.out.println("now enter you desired values for each field: ");
                getNewCategoryFields(editProductController);
                editProductController.setFieldsOfCategory(product);
                System.out.println("edit request was sent successfully");
                return;
            } catch (EditProductController.SameCategoryException e) {
                System.err.println("your product is already under this category.");
            } catch (EditProductController.InvalidCategoryException e) {
                System.err.println("there's no category with this name.");
            }
        }
    }

    private void getNewCategoryFields(EditProductController editProductController) {
        for (Field field : editProductController.getNeededFields()) {
            while (true) {
                System.out.println("    " + field.getName() + ": ");
                String value = scanner.nextLine();
                if (value.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                } else if (value.equalsIgnoreCase("logout")) {
                    logoutChangeMenu();
                }
                try {
                    editProductController.setEachCategoryField(value, field);
                    break;
                } catch (EditProductController.InvalidFieldValue e) {
                    System.err.println("you can only enter numbers for this field");
                }
            }
        }
    }

    private void editProductCategoryField(Product product, EditProductController editProductController) {
        int number = 1;
        for (Field field : editProductController.getCategoryFieldsToEdit(product)) {
            System.out.println(number + ") " + field.getName());
            number += 1;
        }
        System.out.println("select the field you want to edit and when you're done enter end: ");
        String selectedField = scanner.nextLine();
        while (!selectedField.equalsIgnoreCase("end")) {
            if (selectedField.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (selectedField.equalsIgnoreCase("logout")) {
                logoutChangeMenu();
            }
            try {
                Field field = editProductController.getFieldToEdit(selectedField, product);
                while (true) {
                    try {
                        System.out.println("enter your desired new value for " + field.getName() + ": ");
                        String newValue = scanner.nextLine();
                        if (newValue.equalsIgnoreCase("back")) {
                            this.parentMenu.execute();
                        } else if (newValue.equalsIgnoreCase("logout")) {
                            logoutChangeMenu();
                        }
                        editProductController.setEditedField(newValue, field);
                        break;
                    } catch (EditProductController.InvalidFieldValue e) {
                        System.err.println("you can only enter numbers for this field");
                    }
                }
            } catch (EditProductController.InvalidFieldException e) {
                System.err.println("there's no such field for this product");
            }
            selectedField = scanner.nextLine();
        }
    }
}
