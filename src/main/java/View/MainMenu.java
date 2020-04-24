package View;

public class MainMenu extends Menu {

    public MainMenu() {
        super("MainMenu",null);
        this.subMenus.put("user\\s+area",new UserAreaMenu(this));
    }


    @Override
    public void help() {
        System.out.println("user area");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine();
        Menu chosenMenu = null;
        for (String subMenu : subMenus.keySet()) {
            if(input.matches(subMenu)){
                chosenMenu = subMenus.get(subMenu);
            }
        }
        if(chosenMenu != null)
            chosenMenu.execute();
        else{
            System.err.println("Invalid input! Try again please");
            this.execute();
        }
    }
}
