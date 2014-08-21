package ee.golive.finants.model;

public class AccountSum {

    private long sum;
    private String account_guid;
    private String account_type;
    private long year;
    private long month;

    public AccountSum() {

    }

    public AccountSum(long sum, String account_guid) {
        this.setSum(sum);
        this.setAccount_guid(account_guid);
    }

    public AccountSum(long sum, String account_guid, String account_type, int year, int month) {
        this.setSum(sum);
        this.setAccount_guid(account_guid);
        this.setYear(year);
        this.setMonth(month);
        this.setAccount_type(account_type);
    }

    public long getSum() {
        if (this.account_type != null && this.account_type.equals("INCOME")) {
            return sum*-1;
        }
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public String getAccount_guid() {
        return account_guid;
    }

    public void setAccount_guid(String account_guid) {
        this.account_guid = account_guid;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public float getSumMoney() {
        return ((float)this.getSum())/100f;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Account: " + getAccount_guid());
        result.append("Year: " + getYear());
        result.append("Month: " + getMonth());
        result.append("Sum: " + getSum());
        result.append("\n");
        return result.toString();
    }
}
