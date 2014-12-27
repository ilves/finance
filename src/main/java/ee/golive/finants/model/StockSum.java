package ee.golive.finants.model;

import java.util.Date;

public class StockSum {

    private Long amount;
    private String account_guid;
    private String commodity_guid;
    private Date date;
    private String name;

    public StockSum() {

    }

    public StockSum(Long sum, String account_guid, String commodity_guid, Date date) {
        this.setAmount(sum);
        this.setAccount_guid(account_guid);
        this.setDate(date);
        this.setCommodity_guid(commodity_guid);
    }

    public StockSum(Long sum, String account_guid, String commodity_guid, Date date, String name) {
        this.setAmount(sum);
        this.setAccount_guid(account_guid);
        this.setDate(date);
        this.setCommodity_guid(commodity_guid);
        this.setName(name);
    }
    public String getCommodity_guid() {
        return commodity_guid;
    }

    public void setCommodity_guid(String commodity_guid) {
        this.commodity_guid = commodity_guid;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccount_guid() {
        return account_guid;
    }

    public void setAccount_guid(String account_guid) {
        this.account_guid = account_guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Account: " + getAccount_guid());
        result.append("Date: " + getDate());
        result.append("Sum: " + getAmount());
        result.append("\n");
        return result.toString();
    }
}
