package Models.Gifts.Actions;

import Models.WaitingLog;



public interface Action {

    public void action(WaitingLog waitingLog);
    public String toString();
}
