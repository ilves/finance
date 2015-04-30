package ee.golive.finants.model;

/**
 * Created by Taavi on 6.02.2015.
 */
public class BalticStock {
    public String code;
    public Float price;
    public Integer totalStock;
    public Integer profit;

    public String getCode() {
        return code;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getProfit() {
        return profit;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public String toString() {
        return "Code: " + code + " Price: " + price;
    }
}
