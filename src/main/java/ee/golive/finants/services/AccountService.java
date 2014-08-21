package ee.golive.finants.services;


import ee.golive.finants.model.Account;

import java.util.List;

public interface AccountService {
    public List<Account> getList();
    public Account findByGuid(String guid);
    public Account getRootAccount();
    public double getAccountTotal(Account account);
}
