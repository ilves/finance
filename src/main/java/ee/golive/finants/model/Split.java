package ee.golive.finants.model;

import javax.persistence.*;

@Entity
@Table(name="splits")
public class Split {

    @Id
    private String guid;

    private String account_guid;
    private Integer value_num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_guid", nullable = false, insertable = false, updatable = false)
    private Account account;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tx_guid")
    private Transaction transaction;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAccount_guid() {
        return account_guid;
    }

    public void setAccount_guid(String account_guid) {
        this.account_guid = account_guid;
    }

    public Integer getValue_num() {
        return value_num;
    }

    public void setValue_num(Integer value_num) {
        this.value_num = value_num;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }
}
