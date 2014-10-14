package ee.golive.finants.dao;

import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Account findByGuid(String guid) {
        Account account = (Account) this.entityManager.find(Account.class, guid);
        return account;
    }

    @Override
    @Transactional
    public List<Account> getList() {
        List<Account> accountList = this.entityManager.createQuery("from Account").getResultList();
        return accountList;
    }

    @Override
    @Transactional
    public Account getRootAccount() {
        Account account = (Account) this.entityManager.createQuery(
                "from Account where account_type = 'ROOT' AND name = 'Root Account'").getSingleResult();
        return getAccount(account.getGuid());
    }

    @Override
    public Account getAccount(String guid) {
        List<Account> accountList = this.entityManager.createQuery("from Account").getResultList();
        this.entityManager.createNamedQuery("fullAccountTree").getResultList();
        return this.entityManager.find(Account.class, guid);
    }

    @Override
    @Transactional
    public Long getAccountTotal(Account account) {
        Long sum = 0l;
        if (!account.getChildAccounts().isEmpty()) {
            for (Account child : account.getChildAccounts()) {
                sum+=getAccountTotal(child);
            }
        }
        Long sumInt = (Long)this.entityManager.createQuery("select sum(value_num) FROM Split where account_guid = :aq")
                .setParameter("aq", account.getGuid()).getSingleResult();
        if (sumInt != null) {
            sum+= sumInt;
        }
        account.setSum(sum);
        return account.getSum();
    }

    @Override
    public List<AccountSum> getAccountTotals() {
        return this.entityManager.createQuery(
                "select new ee.golive.finants.model.AccountSum(SUM(s.value_num) AS sum, s.account_guid) " +
                "from Split AS s " +
                "group by s.account_guid").getResultList();
    }

    @Override
    public List<AccountSum> getAccountTotalsMonthlyInterval(List<Account> accounts) {
        String[] ids = new String[accounts.size()];
        int n = 0;
        for(Account account : accounts) {
            ids[n] = "'"+account.getGuid()+"'";
            n++;
        }
        return this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num*IFNULL(p.value_num,1)) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year, MONTH(t.post_date) AS month) " +
                "from Split AS s " +
                "join s.transaction as t " +
                "join s.account as a " +
                "left join a.prices as p " +
                "where s.account_guid in ("+implode(", ", ids)+") " +
                "group by YEAR(t.post_date), MONTH(t.post_date) " +
                "order by YEAR(t.post_date) asc, MONTH(t.post_date) asc").getResultList();
    }

    @Override
    public List<AccountSum> getAccountTotalsYearlyInterval(List<Account> accounts) {
        String[] ids = new String[accounts.size()];
        int n = 0;
        for(Account account : accounts) {
            ids[n] = "'"+account.getGuid()+"'";
            n++;
        }
        return this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "left join a.price as p " +
                        "where s.account_guid in ("+implode(", ", ids)+") " +
                        "group by YEAR(t.post_date)" +
                        "order by YEAR(t.post_date) asc").getResultList();
    }

    @Override
    public List<AccountSum> getAccountTotalsMonthlyIntervalDesc(List<Account> accounts, String description) {
        String[] ids = new String[accounts.size()];
        int n = 0;
        for(Account account : accounts) {
            ids[n] = "'"+account.getGuid()+"'";
            n++;
        }
        return this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year, MONTH(t.post_date) AS month) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") AND t.description LIKE '%" + description + "'" +
                        "group by YEAR(t.post_date), MONTH(t.post_date) " +
                        "order by YEAR(t.post_date) asc, MONTH(t.post_date) asc").getResultList();
    }

    @Override
    public List<AccountSum> getAccountTotalsYearlyIntervalDesc(List<Account> accounts, String description) {
        String[] ids = new String[accounts.size()];
        int n = 0;
        for(Account account : accounts) {
            ids[n] = "'"+account.getGuid()+"'";
            n++;
        }
        return this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") AND t.description LIKE '%" + description + "'" +
                        "group by YEAR(t.post_date)" +
                        "order by YEAR(t.post_date) asc").getResultList();
    }

    public static String implode(String glue, String[] strArray)
    {
        String ret = "";
        for(int i=0;i<strArray.length;i++)
        {
            ret += (i == strArray.length - 1) ? strArray[i] : strArray[i] + glue;
        }
        return ret;
    }
}