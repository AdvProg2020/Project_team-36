package Models.Gifts.Events;

import Models.WaitingLog;

import java.util.Date;
//Give customer the discount percent in that log
public class HighPriceEvent implements Event{
    private Date start;
    private Date end;
   private  Long minimumLogPrice;


    @Override
    public Boolean isEventActive(WaitingLog waitingLog) {
        if(this.checkDate()){
            return waitingLog.getTotalPrice() >= minimumLogPrice;
        }else
        return false;
    }

    public boolean checkDate(){
        Date now = new Date();
        return (now.after(this.start) && now.before(this.end)) || now.equals(this.start) || now.equals(this.end);
    }
}
