package ee.golive.finants.helper;

import ee.golive.finants.chart.Point;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import in.satpathy.financial.XIRR;
import in.satpathy.financial.XIRRData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AccountHelper {

    public static void addSumsToAccountTree(Account root, List<AccountSum> sums) {
        addSumToAccount(root, sums);
    }

    public static void addSumToAccount(Account parent, List<AccountSum> sums) {
        Long sum = 0l;
        if (!parent.getChildAccounts().isEmpty()) {
            for(Account child : parent.getChildAccounts()) {
                addSumToAccount(child, sums);
                sum+=child.getSum();
            }
        }
        AccountSum sumObj = findCorrespondingAccountSum(parent, sums);
        sum+=sumObj.getSum();
        parent.setSum(sum);
    }

    public static AccountSum findCorrespondingAccountSum(Account account, List<AccountSum> sums) {
        AccountSum correspondingSum = new AccountSum();
        for(AccountSum item : sums) {
            if (item.getAccount_guid().equals(account.getGuid())) {
                correspondingSum = item;
                sums.remove(item);
                break;
            }
        }
        return correspondingSum;
    }

    public static List<Account> getAllSiblings(Account account) {
        List<Account> siblings = new ArrayList<Account>();
        return getAllSiblings(account, siblings);
    }

    public static List<Account> getAllSiblings(Account account, List<Account> list) {
        list.add(account);
        if (!account.getChildAccounts().isEmpty()) {
            for(Account child : account.getChildAccounts()) {
                list = getAllSiblings(child, list);
            }
        }
        return list;
    }

    public static List<Point> transformAccountSum(List<AccountSum> list, Calendar start, int type) {
        return transformAccountSum(list, false, start, type);
    }

    public static List<Float> transformAccountSum(List<AccountSum> list) {
        return transformAccountSum(list, false);
    }

    public static List<Float> transformAccountSum(List<AccountSum> list, boolean abs) {
        List<Float> ret = new ArrayList<Float>();
        for(AccountSum elem : list) {
            ret.add((abs == true ? Math.abs(elem.getSumMoney()) : elem.getSumMoney()));
        }
        return ret;
    }



    public static List<Point> transformAccountSum(List<AccountSum> list, boolean abs, Calendar start, int type) {
        List<Point> ret = new ArrayList<>();
        for(AccountSum elem : list) {
            ret.add(new Point(start.getTime().getTime(), abs == true ? Math.abs(elem.getSumMoney()) : elem.getSumMoney()));
            start.add(type, 1);
        }
        return ret;
    }

    public static void addInSeries(List<AccountSum> list) {
        Long tmp = 0l;
        for(AccountSum elem : list) {
            tmp+=elem.getSum();
            elem.setSum(tmp);
        }
    }

    public static Long sumList(List <AccountSum> list) {
        Long sum = 0l;
        for(AccountSum elem : list) {
            sum+=elem.getSum();
        }
        return sum;
    }

    public static List<Float> transformAccountDifference(List<AccountSum> assets, List<AccountSum> liab) {
        List<Float> ret = new ArrayList<Float>();
        int i;
        for(i = 0; i < assets.size(); i++) {
            AccountSum a = assets.get(i);
            AccountSum b = liab.get(i);
            ret.add(a.getSumMoney()+b.getSumMoney());
        }
        return ret;
    }

    public static List<Point> transformAccountDifference(List<AccountSum> assets, List<AccountSum> liab, Calendar start, int type) {
        List<Point> ret = new ArrayList<>();
        int i;
        for(i = 0; i < assets.size(); i++) {
            AccountSum a = assets.get(i);
            AccountSum b = liab.get(i);
            ret.add(new Point(start.getTime().getTime(), a.getSumMoney()+b.getSumMoney()));
            start.add(type, 1);
        }
        return ret;
    }

    public static List<AccountSum> sum(List<List<AccountSum>> sum) {
        List<AccountSum> ret = new ArrayList<>();
        for(int n = 0; n < sum.get(0).size(); n++) {
            long total = 0;
            for(int m = 0; m < sum.size(); m++) {
                total = total + sum.get(m).get(n).getSum();
            }
            AccountSum tmp = new AccountSum(total, "Lisatud", "", (int)sum.get(0).get(n).getYear(), (int)sum.get(0).get(n).getMonth());
            ret.add(tmp);
        }
        return ret;
    }

    public static List<Float> sumFloat(List<List<AccountSum>> sum) {
        List<Float> ret = new ArrayList<>();
        for(int n = 0; n < sum.get(0).size(); n++) {
            float total = 0;
            for(int m = 0; m < sum.size(); m++) {
                total = total + sum.get(m).get(n).getSum();
            }
            ret.add(total);
        }
        return ret;
    }

    public static List<Float> calculateXIRRSeries(List<AccountSum> a, List<AccountSum> b, List<Calendar> i) {
        List<Float> ret = new ArrayList<Float>();

        List<Long> amounts = new ArrayList<Long>();
        List<Integer> dates = new ArrayList<Integer>();

        for(Calendar d : i) {
            for(AccountSum n : a) {
                if (n.getYear() < d.get(Calendar.YEAR) ||
                        (n.getYear() == d.get(Calendar.YEAR) && n.getMonth() <= d.get(Calendar.MONTH))) {
                    amounts.add(n.getSum());
                    dates.add(XIRRData.getExcelDateValue(new GregorianCalendar((int) n.getYear(), (int) n.getMonth() - 1, 15)));
                }
            }

            AccountSum last = new AccountSum(0,"","",(int)a.get(0).getYear(),(int)a.get(0).getMonth());
            for(AccountSum n : b) {
                if (n.getYear() < d.get(Calendar.YEAR) ||
                        (n.getYear() == d.get(Calendar.YEAR) && n.getMonth() <= d.get(Calendar.MONTH))) {
                    last = n;
                }
            }

            amounts.add(last.getSum()*-1);
            dates.add(XIRRData.getExcelDateValue(new GregorianCalendar((int) last.getYear(), (int) last.getMonth() - 1, 15)));



            XIRRData data = new XIRRData( amounts.size(), 0.1, listToDouble(amounts), listToDouble2(dates)) ;
            float val = (float)XIRR.xirr(data)*100f;
            ret.add(val == -90f ? 0 : val);

            amounts.clear();
            dates.clear();
        }

        return ret;
    }

    public static double[] listToDouble(List<Long> list) {
        double[] values = new double[list.size()];
        int n = 0;
        for(Long num : list) {
            values[n] = num;
            n++;
        }

        return values;
    }

    public static double[] listToDouble2(List<Integer> list) {
        double[] values = new double[list.size()];
        int n = 0;
        for(Integer num : list) {
            values[n] = num.doubleValue();
            n++;
        }
        return values;
    }
}
