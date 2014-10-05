package ee.golive.finants.services;


import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;

import java.util.List;

public interface AccountService {
    public List<Account> getList();
    public Account findByGuid(String guid);
    public Account getRootAccount();
    public void addAccountTotalsToTree(Account rootAccount);
    public List<AccountSum> getStats(Account account, String step);
    public List<List<AccountSum>> getStats(List<Account> accounts, String step);
    public List<List<AccountSum>> getStats(List<Account> accounts, String step, String description);
    public List<AccountSum> getStatsTotal(List<Account> accounts, String step);
    public List<AccountSum> getStatsTotalWithoutSiblings(List<Account> accounts, String step, String description);
    public List<Account> getAccounts(List<String> accountGuids);
}
