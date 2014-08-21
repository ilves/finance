package ee.golive.finants.helper;

import ee.golive.finants.model.AccountSum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartHelper {
    public static List<Float> transformAccountSum(List<AccountSum> list) {
        List<Float> ret = new ArrayList<Float>();
        for(AccountSum elem : list) {
            ret.add(elem.getSumMoney());
        }
        return ret;
    }

    public static List<String> transformCalendar(List<Calendar> intervals, SimpleDateFormat format) {
        List<String> ret = new ArrayList<String>();
        for(Calendar interval : intervals) {
            ret.add(format.format(interval.getTime()));
        }
        return ret;
    }

    public static List<Calendar> getIntervalList(Date start, Date end) {
        List<Calendar> ret = new ArrayList<Calendar>();
        Calendar date = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(end);
        date.setTime(start);
        date.set(Calendar.DAY_OF_MONTH, 1);

        while(date.getTimeInMillis() < endDate.getTimeInMillis()) {
            ret.add((Calendar) date.clone());
            date.add(Calendar.MONTH, 1);
        }

        return ret;
    }

    public static List<AccountSum> sync(List<AccountSum> sums, List<Calendar> interval) {
        List<AccountSum> ret = new ArrayList<AccountSum>();
        for(Calendar d : interval) {
            Boolean found = false;
            for(AccountSum sum : sums) {
                if (sum.getYear() == d.get(Calendar.YEAR) && sum.getMonth() == d.get(Calendar.MONTH)+1) {
                    found = true;
                    ret.add(sum);
                    sums.remove(sum);
                    break;
                }
            }
            if (!found) {
                ret.add(new AccountSum(0, "", "", d.get(Calendar.YEAR), d.get(Calendar.MONTH)));
            }
        }
        return ret;
    }
}
