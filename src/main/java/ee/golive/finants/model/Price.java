package ee.golive.finants.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="prices")
public class Price {

    @Id
    private String guid;
    private Long value_num;
    private Date date;
    private Long value_denom;
    private String commodity_guid;

    public String getCommodity_guid() {
        return commodity_guid;
    }

    public void setCommodity_guid(String commodity_guid) {
        this.commodity_guid = commodity_guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }



    public Long getValue_num() {
        return value_num;
    }

    public void setValue_num(Long value_num) {
        this.value_num = value_num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date post_date) {
        this.date = date;
    }

    public Long getValue_denom() {
        return value_denom;
    }

    public void setValue_denom(Long value_denom) {
        this.value_denom = value_denom;
    }

    public double getPrice() {
        return (double)getValue_num()/(double)getValue_denom();
    }

    public String toString() {
        return "Date: " + date + " Price: " + getPrice();
    }
}
