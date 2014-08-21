package ee.golive.finants.dao;

import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;

import java.util.List;

public interface AccountDao {
    public Account findByGuid(String guid);
    public List<Account> getList();
    public Account getRootAccount();
    public Account getAccount(String guid);
    public Long getAccountTotal(Account account);
    public List<AccountSum> getAccountTotals();
    public List<AccountSum> getAccountTotalsMonthlyInterval(List<Account> accounts);
}