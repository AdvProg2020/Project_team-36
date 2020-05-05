package Models.Gifts;

import Models.Gifts.Actions.Action;
import Models.Gifts.Events.Event;
import Models.WaitingLog;

import java.util.ArrayList;

public class Gift {
    private Action action;
    private Event event;
    private static ArrayList<Gift> allGifts;


    public Gift(Action action, Event event) {
        this.action = action;
        this.event = event;
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

    public boolean includedInEvent(WaitingLog waitingLog){
        return event.isEventActive(waitingLog);
    }

    public void perform(WaitingLog waitingLog){
        action.action(waitingLog);
    }
}
