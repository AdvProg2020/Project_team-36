package View;

import Controllers.GiftController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;

public class GiftMenu extends Menu {

    public GiftMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("first\\s+buy\\s+gift",addFirstBuyGift());
        subMenus.put("high\\s+log\\s+price\\s+gift",addHighLogPriceGift());
        subMenus.put("periodic\\s+gift",addPeriodicGift());
    }

    @Override
    public void help() {
        System.out.println("you can add new gifts here with these commands:\n" +
                "fist buy gift -> for when a customer buys for the first time here.\n" +
                "high log price gift -> for when the total price of a purchase is more than an amount you want.\n" +
                "periodic gift -> to give gifts to customer for an special date");
    }

    @Override
    public void execute() {
        HashMap<Integer,String> eventName = managerController.getGiftEventsName();
        Matcher matcher;
        Menu chosenMenu = null;
        String input;
        System.out.println("choose the event you want to create gift for:");
        int i =1;
        for (String name : eventName.values()) {
            System.out.println(i+") "+name);
            i++;
        }
        input = scanner.nextLine().trim();
            while (!((input.matches("back")) || (input.matches("help")))) {
                for (String regex : subMenus.keySet()) {
                    matcher = getMatcher(input, regex);
                    if (matcher.matches()) {
                        chosenMenu = subMenus.get(regex);
                        break;
                    }
                }
                if (chosenMenu == null) {
                    System.err.println("Invalid command! Try again please");
                } else {

                    chosenMenu.execute();
                }
                input = scanner.nextLine().trim();
            }
            if (input.matches("back")) {
                this.parentMenu.execute();
            } else if (input.matches("help")) {
                this.help();
                this.execute();
            }
        }

    private Menu addFirstBuyGift() {
        return new Menu("add first buy gift", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                GiftController giftController = new GiftController();
                System.out.println("enter the name for this gift: ");
                giftController.setName(scanner.nextLine().trim());
                setStartDateForEvent(giftController);
                setEndDateForEvent(giftController);
                System.out.println("choose the action your gift is going to do:\n" +
                        "give discount code\n" +
                        "give discount in current log");
                while (true){
                    String input = scanner.nextLine().trim();
                    if(input.matches("back")) {
                        this.parentMenu.execute();
                    }else if(input.matches("give\\s+discount\\s+code")){
                        addGiveDiscountCodeAsAction(giftController);
                        giftController.newFirstBuyGiftWithDiscountCode();
                        return;
                    } else if (input.matches("give\\s+discount\\s+in\\s+current\\s+log")){
                        addDiscountInCurrentLogAsAction(giftController);
                        giftController.newFirstBuyGiftWithDiscountInLog();
                        return;
                    } else {
                        System.out.println("invalid command. try again.");
                    }
                }
            }
        };

    }

    private Menu addHighLogPriceGift() {
        return new Menu("add high log price gift", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                GiftController giftController = new GiftController();
                System.out.println("enter the name for this gift: ");
                giftController.setName(scanner.nextLine().trim());
                setStartDateForEvent(giftController);
                setEndDateForEvent(giftController);
                setMinimumLogPriceForGift(giftController);
                System.out.println("choose the action your gift is going to do:\n" +
                        "give discount code\n" +
                        "give discount in current log");
                while (true){
                    String input = scanner.nextLine().trim();
                    if(input.matches("back")) {
                        this.parentMenu.execute();
                    }else if(input.matches("give\\s+discount\\s+code")){
                        addGiveDiscountCodeAsAction(giftController);
                        giftController.newHighPriceGiftWithDiscountCode();
                        return;
                    } else if (input.matches("give\\s+discount\\s+in\\s+current\\s+log")){
                        addDiscountInCurrentLogAsAction(giftController);
                        giftController.newHighPriceGiftWithDiscountInLog();
                        return;
                    } else {
                        System.out.println("invalid command. try again.");
                    }
                }

            }
        };

    }

    private Menu addPeriodicGift() {
        return new Menu("add periodic gift", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                GiftController giftController = new GiftController();
                System.out.println("enter the name for this gift: ");
                giftController.setName(scanner.nextLine().trim());
                setStartDateForEvent(giftController);
                setEndDateForEvent(giftController);
                System.out.println("choose the action your gift is going to do:\n" +
                        "give discount code\n" +
                        "give discount in current log");
                while (true){
                    String input = scanner.nextLine().trim();
                    if(input.matches("back")) {
                        this.parentMenu.execute();
                    }else if(input.matches("give\\s+discount\\s+code")){
                        addGiveDiscountCodeAsAction(giftController);
                        giftController.newPeriodicGiftWithDiscountCode();
                        return;
                    } else if (input.matches("give\\s+discount\\s+in\\s+current\\s+log")){
                        addDiscountInCurrentLogAsAction(giftController);
                        giftController.newPeriodicGiftWithDiscountInLog();
                        return;
                    } else {
                        System.out.println("invalid command. try again.");
                    }
                }
            }
        };

    }

    private void addGiveDiscountCodeAsAction(GiftController giftController){
        CreateDiscountCodeMenu createDiscountCodeMenu = new CreateDiscountCodeMenu(this.parentMenu);
        giftController.setDiscount(createDiscountCodeMenu.newCodeForGift());
    }

    private void addDiscountInCurrentLogAsAction(GiftController giftController){
        setDiscountPercentageForAction(giftController);
        setDiscountLimitForAction(giftController);
    }

    private void setStartDateForEvent(GiftController giftController){
        String input;
        while(true) {
            System.out.println("start date in yyyy/MM/dd format :");
            input = scanner.nextLine();
            if(input.equals("back")){
                this.parentMenu.execute();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date startDate;
            try {
                startDate = dateFormat.parse(input);
                giftController.setStartDate(startDate);
                return;
            } catch (ParseException e) {
                System.err.println("input isn't in the yyyy/MM/dd format");
            }
        }
    }

    private void setEndDateForEvent(GiftController giftController){
        String input;
        while(true) {
            System.out.println("end date in yyyy/MM/dd format:");
            input = scanner.nextLine();
            if(input.equals("back")){
                this.parentMenu.execute();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date endDate;
            try {
                endDate = dateFormat.parse(input);
                try {
                    giftController.setEndDate(endDate);
                    return;
                }catch (GiftController.AlreadyPastDateException | GiftController.EndBeforeStartDateException dateError){
                    System.err.println(dateError.getMessage());
                }
            } catch (ParseException formatError) {
                System.err.println("input isn't in the yyyy/MM/dd format");
            }
        }
    }

    private void  setDiscountPercentageForAction(GiftController giftController){
        int percentage;
        String input;
        while (true){
            System.out.println("discount percentage between 0-100 :");
            try {
                input=scanner.nextLine();
                if(input.equals("back")){
                    this.parentMenu.execute();
                }
                percentage = Integer.parseInt(input);
                if (percentage < 100 && percentage > 0) {
                    giftController.setDiscountPercent(percentage * 0.01);
                    return;
                } else {
                    System.err.println("number not in the desired range");
                }
            }catch (NumberFormatException e){
                System.err.println("you can't enter anything but number");
            }
        }
    }

    private void  setDiscountLimitForAction(GiftController giftController){
        long limit;
        String input;
        while (true){
            System.out.println("discount limit :");
            try {
                input=scanner.nextLine();
                if(input.equals("back")){
                    this.parentMenu.execute();
                }
                limit = Long.parseLong(input);
                giftController.setDiscountLimit(limit);
                return;
            }catch (NumberFormatException e){
                System.err.println("you can't enter anything but number");
            }
        }
    }

    private void  setMinimumLogPriceForGift(GiftController giftController){
        long minimumPrice;
        String input;
        while (true){
            System.out.println("minimum log price for gift :");
            try {
                input=scanner.nextLine();
                if(input.equals("back")){
                    this.parentMenu.execute();
                }
                minimumPrice = Long.parseLong(input);
                giftController.setMinimumLogPrice(minimumPrice);
                return;
            }catch (NumberFormatException e){
                System.err.println("you can't enter anything but number");
            }
        }
    }

}
