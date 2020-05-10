package Models.Gifts.Events;

import Models.WaitingLog;

import java.util.Date;

public class FirstBuyEvent implements Event {
    private Date start;
    private Date end;

    @Override
    public Boolean isEventActive(WaitingLog waitingLog) {
        if(this.checkDate()){
            return waitingLog.getCustomer().getAllLogs().isEmpty();
        }
        else
            return false;
    }

    public boolean checkDate(){
        Date now = new Date();
        return (now.after(this.start) && now.before(this.end)) || now.equals(this.start) || now.equals(this.end);
    }




}
