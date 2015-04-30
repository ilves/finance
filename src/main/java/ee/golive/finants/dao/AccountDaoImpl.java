package ee.golive.finants.dao;

import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Price;
import ee.golive.finants.model.StockSum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Account findByGuid(String guid) {
        return (Account) this.entityManager.find(Account.class, guid);
    }

    @Override
    @Transactional
    public List<Account> getList() {
        return this.entityManager.createQuery("from Account").getResultList();
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
                "where s.account_guid NOT IN('c6088213aa199e60d27e74d1f1bfdc37') " +
                "group by s.account_guid").getResultList();
    }




    @Override
    public List<AccountSum> getTotalsMonthlyInterval(List<Account> accounts) {
        String[] ids = new String[accounts.size()];
        int n = 0;
        for(Account account : accounts) {
            ids[n] = "'"+account.getGuid()+"'";
            n++;
        }

        List<AccountSum> moneyAccs = this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year, MONTH(t.post_date) AS month, a.name AS name) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+")" +
                        "group by YEAR(t.post_date), MONTH(t.post_date) " +
                        "order by YEAR(t.post_date) asc, MONTH(t.post_date) asc").getResultList();

        return moneyAccs;
    }
    private List<StockSum> getExistingStock(List<StockSum> stock, AccountSum account) {
        Map<String, StockSum> ret = new HashMap<>();
        for(StockSum s : stock) {
            if (s.getDate().compareTo(account.getDate()) <= 0) {
                if (ret.containsKey(s.getAccount_guid())) {
                    StockSum old = ret.get(s.getAccount_guid());
                    old.setAmount(old.getAmount()+s.getAmount());
                    old.setDate(s.getDate());
                } else {
                    ret.put(s.getAccount_guid(), new StockSum(s.getAmount(), s.getAccount_guid(), s.getCommodity_guid(), s.getDate(), s.getName()));
                }
            }
        }
        return new ArrayList<>(ret.values());
    }

    private Price getSuitablePrice(StockSum stock, List<Price> prices, AccountSum account) {
        Price tmp = new Price();
        for (Price p : prices) {
            if (!stock.getCommodity_guid().equals(p.getCommodity_guid())) continue;
            if (p.getDate().compareTo(account.getDate()) <= 0) {
                tmp = p;
            }
        }
        return tmp;
    }

    private String[] getIds(List<Account> accounts)  {
        String[] ids = new String[accounts.size()];
        int n = 0;
        for(Account account : accounts) {
            ids[n] = "'"+account.getGuid()+"'";
            n++;
        }
        return ids;
    }

    @Override
    public List<AccountSum> getAccountTotalsMonthlyInterval(List<Account> accounts) {
        String[] ids = getIds(accounts);

        List<AccountSum> moneyAccs = this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year, MONTH(t.post_date) AS month, a.name AS name) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") AND a.account_type NOT IN ('STOCK')" +
                        "group by YEAR(t.post_date), MONTH(t.post_date) " +
                        "order by YEAR(t.post_date) asc, MONTH(t.post_date) asc").getResultList();


        List<Price> prices = this.entityManager.createQuery(
                "from Price group by commodity_guid, YEAR(date), MONTH(date)").getResultList();

        List<StockSum> amountAccs = this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.StockSum(" +
                        "SUM(s.quantity_num/s.quantity_denom) AS sum, s.account_guid, a.commodity_guid, t.post_date, a.name AS name) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") AND a.account_type IN ('STOCK')" +
                        "group by s.account_guid, YEAR(t.post_date), MONTH(t.post_date) " +
                        "order by t.post_date").getResultList();


        if (moneyAccs.size() == 0) {
            Date last = null;
            for(Price p : prices) {
                if (last != null && p.getDate().compareTo(last) <= 0) continue;
                last = p.getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(p.getDate());
                moneyAccs.add(new AccountSum(0, "", "", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1));
            }


        }

        long last = 0;
        for(AccountSum a : moneyAccs) {
            List<StockSum> stocks = getExistingStock(amountAccs, a);
            long value = 0;
            StockSum tmp = new StockSum();
            for(StockSum e : stocks) {
                tmp = e;
                value+=(long)(getSuitablePrice(e, prices, a).getPrice()*e.getAmount()*100);
            }
            //System.out.println(value-last);
            a.add(value-last);
            a.setName(tmp.getName());
            last = value;
        }

        return moneyAccs;
    }

    @Override
    public List<AccountSum> getAccountTotalsYearlyInterval(List<Account> accounts) {
        String[] ids = getIds(accounts);

        List<AccountSum> moneyAccs = this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.AccountSum(" +
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") AND a.account_type NOT IN ('STOCK')" +
                        "group by YEAR(t.post_date)" +
                        "order by YEAR(t.post_date) asc").getResultList();


        List<Price> prices = this.entityManager.createQuery(
                "from Price group by commodity_guid, YEAR(date), MONTH(date)").getResultList();

        List<StockSum> amountAccs = this.entityManager.createQuery(
                "select " +
                        "new ee.golive.finants.model.StockSum(" +
                        "SUM(s.quantity_num/s.quantity_denom) AS sum, s.account_guid, a.commodity_guid, t.post_date, a.name AS name) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") AND a.account_type IN ('STOCK')" +
                        "group by s.account_guid, YEAR(t.post_date) " +
                        "order by t.post_date").getResultList();

        if (moneyAccs.size() == 0) {
            Date last = null;
            for(Price p : prices) {
                if (last != null && p.getDate().compareTo(last) <= 0) continue;
                last = p.getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(p.getDate());
                moneyAccs.add(new AccountSum(0, "", "", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1));
            }
        }


        long last = 0;
        for(AccountSum a : moneyAccs) {
            List<StockSum> stocks = getExistingStock(amountAccs, a);
            long value = 0;
            StockSum tmp = new StockSum();
            for(StockSum e : stocks) {
                tmp = e;
                value+=(long)(getSuitablePrice(e, prices, a).getPrice()*e.getAmount()*100);
            }
            a.add(value-last);
            a.setName(tmp.getName());
            last = value;
        }

        return moneyAccs;

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
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year, MONTH(t.post_date) AS month, a.name AS name)" +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") " + getLike(description) +
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
                        "SUM(s.value_num) AS sum, s.account_guid, a.account_type, YEAR(t.post_date) AS year, a.name AS name) " +
                        "from Split AS s " +
                        "join s.transaction as t " +
                        "join s.account as a " +
                        "where s.account_guid in ("+implode(", ", ids)+") " + getLike(description) +
                        "group by YEAR(t.post_date)" +
                        "order by YEAR(t.post_date) asc").getResultList();
    }

    private String getLike(String description) {
        String sql = "AND (";
        String[] parts = description.split("\\|");
        for(int i = 0; i < parts.length; i++) {
            sql+=" t.description LIKE '%" + parts[i] + "%'";
            if (i < parts.length-1) {
                sql+=" OR ";
            }
        }
        sql+=") ";
        return sql;
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