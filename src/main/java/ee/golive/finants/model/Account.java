package ee.golive.finants.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQuery(name="fullAccountTree", query="select n from Account n left join fetch n.childAccounts")
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String guid;
    private String name;
    private String account_type;
    private String parent_guid;

    @Transient
    private Double sum;

    @ManyToOne
    @JoinColumn(name="parent_guid", insertable=false, updatable=false)
    private Account parentAccount;

    @OneToMany(mappedBy="parentAccount", cascade = CascadeType.ALL)
    @OrderBy("name ASC")
    private Set<Account> childAccounts = new HashSet<Account>();

    @OneToMany(mappedBy = "account")
    private Set<Split> splits = new HashSet<Split>();

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getParent_guid() {
        return parent_guid;
    }

    public void setParent_guid(String parent_guid) {
        this.parent_guid = parent_guid;
    }

    public Set<Account> getChildAccounts() {
        return this.childAccounts;
    }

    public Set<Split> getSplits() {
        return this.splits;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
