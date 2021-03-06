package Models.Gifts.Events;

import Models.WaitingLog;

import java.util.Date;

public class PeriodicEvent implements Event {
    private Date start;
    private Date end;

    public PeriodicEvent(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Boolean isEventActive(WaitingLog waitingLog) {
        Date now = new Date();
        return (now.after(this.start) && now.before(this.end)) || now.equals(this.start) || now.equals(this.end);
    }

    public boolean checkDate(){
        Date now = new Date();
        return (now.after(this.start) && now.before(this.end)) || now.equals(this.start) || now.equals(this.end);
    }

}
