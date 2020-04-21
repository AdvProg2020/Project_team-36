package View;

public class UserAreaMenu extends Menu{
    public UserAreaMenu(Menu parentMenu) {
        super("UserAreaMenu",parentMenu);
        subMenus.put("EntryMenu", new EntryMenu());
    }

    private void newManagerloggedIn(){
        this.subMenus.put("UserMenu",new ManagerMenu());
    }

    private void newCustomerLoggedIn(){
        this.subMenus.put("UserMenu", new CustomerMenu());
    }

    private void newSellerMenu(){
        this.subMenus.put("UserMenu", new SellerMenu());
    }

    private void logout(){
        this.subMenus.remove("UserMenu");
    }

    @Override
    public void help() {}

    @Override
    public void execute() {
        if(subMenus.containsKey("UserMenu")){
            subMenus.get("UserMenu").execute();
        } else{
            subMenus.get("EntryMenu").execute();
        }

    }
}
