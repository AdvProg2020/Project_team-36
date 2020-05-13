package View;

public class ManagerMenu extends Menu {


    public ManagerMenu(Menu parentMenu) {
        super("ManagerMenu",parentMenu);
        //  subMenus.put("view\\s+personal\\s+info\\s+menu", new ViewPersonalInfoMenu(this));
        subMenus.put("manage\\s+categories", new ManageCategoriesMenu(this));
        subMenus.put("manage\\s+requests", new ManageRequestsMenu(this));
        subMenus.put("view\\s+discount\\s+codes", new ViewDiscountCodesMenu(this));
        subMenus.put("manage\\s+users", new ManageUsersMenu(this));
        subMenus.put("manage\\s+all\\s+products", new ManageAllProductsMenu(this));
        subMenus.put("create\\s+discount\\s+code",new CreateDiscountCodeMenu(this));
        subMenus.put("create\\s+new\\s+gift",new GiftMenu("GiftMenu",this));

    }

    @Override
    public void help() {
        System.out.println("view personal info\n" +
                "manage categories\n" +
                "manage requests\n" +
                "view discount codes\n" +
                "manage users\n" +
                "manage all products\n" +
                "create discount code\n");

    }

}




