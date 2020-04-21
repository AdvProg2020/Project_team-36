package View;

import Controllers.EntryController;


import java.util.regex.Matcher;

public class EntryMenu extends Menu{
    public EntryMenu(Menu parentMenu) {
        super("EntryMenu", parentMenu);
    }

    @Override
    public void help() {
        System.out.println("All commands:");
        System.out.println("create account [type] [username]");
        System.out.println("login [username]");
        System.out.println("NOTE: type of the account can be either manager,customer or seller");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if(input.matches("back"))
            parentMenu.execute();
        if((matcher=getMatcher(input,"create\\s+account\\s+(\\w+)\\s+(\\w+)")).matches()){
            try {
                entryController.setUsernameRegister(matcher.group(1),matcher.group(2));
                registerProcess();
            } catch (EntryController.InvalidUsernameException|EntryController.ManagerExistsException|EntryController.InvalidTypeException e) {
                e.getMessage();
                execute();
            }
        }
    }

    private void registerProcess(){
       System.out.println("password:");
       entryController.setPasswordRegister(scanner.nextLine());
        System.out.println("firstname:");


    }

    public void execute(String username){

    }
}
