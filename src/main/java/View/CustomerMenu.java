package View;

public class CustomerMenu extends Menu{
    public CustomerMenu(String name, Menu parentMenu) {
        super("customerMenu", parentMenu);
        subMenus.put("view\\s+personal\\s+info",null);
        subMenus.put("view\\s+cart",new CartMenu("cartMenu",this));
    }

    @Override
    public void help() {
        System.out.println("view personal info\nview cart\nview orders\nview balance\nview discount codes");
    }


}
