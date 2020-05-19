package Models.Gifts;

import Models.Gifts.Actions.Action;

import Models.Gifts.Events.Event;
import Models.WaitingLog;

import java.util.ArrayList;
import java.util.HashSet;


public class Gift {
    private String name;
    private Action action;
    private Event event;
    private static ArrayList<Gift> allGifts = new ArrayList<>();


    public Gift(String name, Action action, Event event) {
        this.name = name;
        this.action = action;
        this.event = event;
        allGifts.add(this);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public Event getEvent() {
        return event;
    }

    public boolean includedInEvent(WaitingLog waitingLog) {
        return event.isEventActive(waitingLog);
    }

    public static void giveGift(WaitingLog waitingLog) {
        ArrayList<Gift> properGifts = new ArrayList<>();
        for (Gift gift : allGifts) {
            if (gift.includedInEvent(waitingLog)) {
                properGifts.add(gift);
                gift.perform(waitingLog);
            }
        }
        waitingLog.setGifts(properGifts);
    }

    public void perform(WaitingLog waitingLog) {
        action.action(waitingLog);
    }

    public static void updateGifts(){
        ArrayList<Gift> toBeRemoved = new ArrayList<>();
        for (Gift gift : allGifts) {
            if(!gift.getEvent().checkDate()){
                toBeRemoved.add(gift);
            }
        }
        allGifts.removeAll(toBeRemoved);
    }


    @Override
    public String toString() {
        return this.name + "\n" + this.action.toString();
    }

    public String getName() {
        return name;
    }


}
