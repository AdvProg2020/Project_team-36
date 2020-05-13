package View;

public class UserAreaMenu extends Menu{
    public UserAreaMenu(Menu parentMenu) {
        super("UserAreaMenu",parentMenu);
        subMenus.put("EntryMenu", new EntryMenu(this));
    }

    public void newUserMenu(String type){
       if(type.matches("manager"))
            this.subMenus.put("UserMenu",new ManagerMenu(this));
        //else if(type.matches("customer"))
           // this.subMenus.put("UserMenu", new CustomerMenu(this));
        //else if(type.matches("seller"))
        //    this.subMenus.put("UserMenu", new SellerMenu(this));
        //this.subMenus.remove("EntryMenu");
    }

    public void logout(){
        this.subMenus.remove("UserMenu");
        subMenus.put("EntryMenu",new EntryMenu(this));
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

    //-..-
}
