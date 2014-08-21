package ee.golive.finants.dao;

import ee.golive.finants.model.Account;

import java.util.List;

public interface AccountDao {
    public Account findByGuid(String guid);
    public List<Account> getList();
    public Account getRootAccount();
    public Double getAccountTotal(Account account);
}