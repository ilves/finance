package ee.golive.finants.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String guid;
    private String description;
    private Date enter_date;
    private Date post_date;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnter_date() {
        return enter_date;
    }

    public void setEnter_date(Date enter_date) {
        this.enter_date = enter_date;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

   // @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    //private Set<Split> splits = new HashSet<Split>();

}
