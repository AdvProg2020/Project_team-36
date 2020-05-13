package View;

import Controllers.ManagerController;
import Models.Discount;
import Models.Request;

import java.util.regex.Matcher;

public class ManageRequestsMenu extends Menu {

    private int id;

    public ManageRequestsMenu(Menu parentMenu) {
        super("ManageRequestsMenu", parentMenu);
        subMenus.put("details\\s+(\\d+)",showRequestDetails());
        subMenus.put("accept\\s+(\\d+)",acceptRequest());
        subMenus.put("decline\\s+(\\d+)",declineRequest());
    }

    @Override
    public void help() {
        System.out.println("details [requestId]\n" +
                "accept [requestId]\n" +
                "decline [requestId\n");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Request request : managerController.getAllRequests()) {
            System.out.println(number + ") " + request.getRequestId());
            number += 1;
        }
        System.out.println("choose the request and what you want to do with it :");
        String input = scanner.nextLine().trim();
        while (!((input.matches("back")) || (input.matches("help")))) {
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
            chosenMenu=null;
            input = scanner.nextLine().trim();
        }
        if (input.matches("back")) {
            this.parentMenu.execute();
        } else if (input.matches("help")) {
            this.help();
            this.execute();
        }
    }

    private Menu showRequestDetails() {
        return new Menu("showRequestDetails", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    Request request = managerController.getRequestWithId(id);
                    System.out.println(request);
                } catch (ManagerController.InvalidRequestIdException e){
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    private Menu declineRequest() {
        return new Menu("declineRequest", this) {
            @Override
            public void help() {
            }

        };
    }

    private Menu acceptRequest() {
        return new Menu("acceptRequest", this) {
            @Override
            public void help() {
            }

        };
    }
}
