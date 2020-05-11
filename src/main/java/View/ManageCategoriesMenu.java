package View;

public class ManageCategoriesMenu extends Menu {


    public ManageCategoriesMenu( Menu parentMenu) {
        super("ManageCategories",parentMenu);

    }

    @Override
    public void help() {
        System.out.println("edit [category]\n" +
                "add [category]\n" +
                "remove [category]\n" +
                "help\n" +
                "logout");
    }





}
