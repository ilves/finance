package ee.golive.finants.helper;

import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartHelper {

    public static List<String> transformCalendar(List<Calendar> intervals, SimpleDateFormat format) {
        List<String> ret = new ArrayList<String>();
        for(Calendar interval : intervals) {
            ret.add(format.format(interval.getTime()));
        }
        return ret;
    }

    public static List<Calendar> getIntervalList(Date start, Date end) {
        return getIntervalList(start, end, "month");
    }

    public static List<Calendar> getIntervalList(Date start, Date end, String step) {
        List<Calendar> ret = new ArrayList<Calendar>();
        Calendar date = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(end);
        endDate.set(Calendar.HOUR, 12);
        date.setTime(start);
        date.set(Calendar.DAY_OF_MONTH, 1);

        if (step.equals("year")) {
            endDate.set(Calendar.MONTH, 11);
        }

        while(date.getTimeInMillis() < endDate.getTimeInMillis()) {
            ret.add((Calendar) date.clone());
            if (step.equals("year")) {
                date.add(Calendar.YEAR, 1);
            } else {
                date.add(Calendar.MONTH, 1);
            }
        }

        return ret;
    }

    public static List<AccountSum> sync(List<AccountSum> sums, List<Calendar> interval) {
        return sync(sums, interval, "month");
    }

    public static List<AccountSum> sync(List<AccountSum> sums, List<Calendar> interval, String step) {
        List<AccountSum> ret = new ArrayList<AccountSum>();
        for(Calendar d : interval) {
            Boolean found = false;
            for(AccountSum sum : sums) {
                if ((step.equals("year") && sum.getYear() == d.get(Calendar.YEAR)) ||
                    (sum.getYear() == d.get(Calendar.YEAR) && sum.getMonth() == d.get(Calendar.MONTH)+1)) {
                    found = true;
                    ret.add(sum);
                    sums.remove(sum);
                    break;
                }
            }
            if (!found) {
                ret.add(new AccountSum(0, "LISATUD", "", d.get(Calendar.YEAR), d.get(Calendar.MONTH)+1));
            }
        }
        return ret;
    }

    public static List<AccountSum> fillCaps(List<AccountSum> sums, List<Calendar> i, String step) {
        List<AccountSum> ret = new ArrayList<AccountSum>();
        AccountSum first = sums.size() > 0 ? sums.get(0) : null;
        List<Calendar> interval = getIntervalList(
                sums.size()>0?parseDate(first.getYear()+"/"+first.getMonth()):i.get(0).getTime(),
                i.get(i.size()-1).getTime(), step);
        return sync(sums, interval, step);
    }

    public static List<AccountSum> difference(List<AccountSum> a, List<AccountSum> b) {
        List<AccountSum> ret = new ArrayList<AccountSum>();
        int i = 0;
        for(AccountSum n : a) {
            long num = b.size() > i ? b.get(i).getSum() : 0;
            long sum = (n.getSum()-num);
            AccountSum tmp = new AccountSum(sum, "", "", (int)n.getYear(), (int)n.getMonth());
            i++;
            ret.add(tmp);
        }
        return ret;
    }

    public static List<Float> differencePrecent(List<AccountSum> a, List<AccountSum> b) {
        List<Float> ret = new ArrayList<Float>();
        int i = 0;
        for(AccountSum n : a) {
            float sum = (float)(n.getSum()-b.get(i).getSum())/n.getSum();
            if (sum == Float.NEGATIVE_INFINITY  || sum == Float.POSITIVE_INFINITY || sum < -1f || Float.isNaN(sum)) sum = 0;
            i++;
            ret.add(sum*100f);
        }
        return ret;
    }

    public static List<Float> differencePrecentSliding(List<AccountSum> a, List<AccountSum> b) {
        List<Float> ret = new ArrayList<Float>();
        int i = 0;
        long asum = 0;
        long bsum = 0;
        for(AccountSum n : a) {
            asum+=n.getSum();
            bsum+=b.get(i).getSum();
            float sum = (float)(asum-bsum)/asum;
            if (sum == Float.NEGATIVE_INFINITY || sum == Float.POSITIVE_INFINITY || sum < -1f  || Float.isNaN(sum)) sum = 0;
            i++;
            ret.add(sum*100f);
        }
        return ret;
    }

    public static Series sumSeries(List<Series> list, String name) {
        List<Float> ret = new ArrayList<Float>();
        for(Series s : list) {
            int x = 0;
            for(Float f : s.getData()) {
                if (ret.size() == x) {
                    ret.add(f);
                } else {
                    ret.set(x, ret.get(x)+f);
                }
                x++;
            }
        }
        Series series = new Series(name, ret);
        return series;
    }

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("y/M").parse(date);
        } catch (ParseException e) {

        }
        return new Date();
    }
}
