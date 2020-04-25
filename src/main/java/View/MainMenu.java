package View;

public class MainMenu extends Menu {

    public MainMenu() {
        super("MainMenu", null);
        this.subMenus.put("user\\s+area", new UserAreaMenu(this));
    }


    @Override
    public void help() {
        System.out.println("user area");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;

        if (input.matches("help")) {
            help();
            execute();
        } else {
            for (String regex : subMenus.keySet()) {
                if (input.matches(regex)) {
                    chosenMenu = subMenus.get(regex);
                    break;
                }
            }
        }
        if (chosenMenu != null)
            chosenMenu.execute();
        else {
            System.err.println("Invalid command! Try again please!");
            this.execute();
        }
    }
}
