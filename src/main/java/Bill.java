import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.logging.Logger;

public class Bill {

    private ZonedDateTime startingTime; //开始通话时间
    private ZonedDateTime endingTime;   //结束通话时间

    public Bill(ZonedDateTime startingTime, ZonedDateTime endingTime) {
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    /**
     * 获取通话时间
     *
     * @return
     */
    public long getHoldingTime() {
        long minute = 0;
        long between;

        ZoneId zoneId = startingTime.getZone();
        ZoneRules zoneRules = zoneId.getRules();

        Boolean isDstOfStart = zoneRules.isDaylightSavings( startingTime.toInstant() );
        Boolean isDstOfEnd = zoneRules.isDaylightSavings( endingTime.toInstant() );

        //System.out.println(startingTime.getHour());
        if(startingTime.getHour()==2 && endingTime.getHour()==2 && (startingTime.getMinute()>endingTime.getMinute())&&isDstOfEnd==false&&isDstOfStart==false)
        {
            between = 3600 - (startingTime.toEpochSecond() - endingTime.toEpochSecond());
        }
        else
            between = endingTime.toEpochSecond() - startingTime.toEpochSecond();  // 返回秒数


        if (between < 0)
            minute = between / 60;// 得到通话时长
        else
            minute = (between + 59) / 60;//不到一分钟，算一分钟


        if (minute > 1800 || minute < 0) {
            throw new RuntimeException("通话时间不正确");
        } else {
            return minute;
        }
    }
    public double PhoneBillCaculator(long min){
        double Bill = 0;
        long extra = 0;
        if(min<=20){
            Bill += 0.05*min;
        }
        else {
            extra = min - 20;
            Bill = 1+extra*0.1;
        }
        System.out.println(Bill);
        return Bill;
    }
}
