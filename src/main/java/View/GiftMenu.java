package View;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GiftMenu extends Menu {

    public GiftMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
    }

    @Override
    public void help() {

    }

    @Override
    public void execute() {
        HashMap<Integer,String> eventName = managerController.getGiftEventsName();
        HashMap<Integer,String> actionNAme = managerController.getGiftActionsName();
        String input;
        System.out.println("choose the type of event you want to create gift by typing number of that:");
        int i =1;
        for (String name : eventName.values()) {
            System.out.println(i+"."+name);
            i++;
        }
        input = scanner.nextLine().trim();
        while(!input.equals("back")||!input.matches("\\d+")||(input.matches("\\d+")&&Integer.parseInt(input)>eventName.size())){
            System.out.println("invalid command!Try again");
        }
        if(input.matches("back"))
            this.getParentMenu().execute();
        getEventInformation(Integer.parseInt(input));
    }

    private void getEventInformation(int event){
        ArrayList<String> data = new ArrayList<>();
        String input;
        System.out.println("write ");

    }
}
