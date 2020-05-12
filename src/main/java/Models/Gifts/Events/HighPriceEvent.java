package Models.Gifts.Events;

import Models.WaitingLog;

import java.util.Date;
//Give customer the discount percent in that log
public class HighPriceEvent implements Event{
    private Date start;
    private Date end;
    private Long minimumLogPrice;


    public HighPriceEvent(Date start, Date end, Long minimumLogPrice) {
        this.start = start;
        this.end = end;
        this.minimumLogPrice = minimumLogPrice;
    }

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
