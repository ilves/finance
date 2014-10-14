package ee.golive.finants.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="prices")
public class Price {

    @Id
    private String guid;
    private String commodity_guid;
    private Long value_num;
    private Date post_date;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "commodity_guid", referencedColumnName="commodity_guid", nullable = false, insertable = false, updatable = false)
    private Set<Account> accounts;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCommodity_guid() {
        return commodity_guid;
    }

    public void setCommodity_guid(String commodity_guid) {
        this.commodity_guid = commodity_guid;
    }

    public Long getValue_num() {
        return value_num;
    }

    public void setValue_num(Long value_num) {
        this.value_num = value_num;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }
}
