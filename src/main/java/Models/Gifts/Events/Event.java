package Models.Gifts.Events;

import Models.WaitingLog;

public interface Event {
    public Boolean isEventActive(WaitingLog waitingLog);


}
