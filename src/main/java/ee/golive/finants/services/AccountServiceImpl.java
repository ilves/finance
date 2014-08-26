package ee.golive.finants.services;

import ee.golive.finants.dao.AccountDao;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountDao accountDao;

    @Override
    public List<Account> getList() {
        return accountDao.getList();
    }

    @Override
    public Account findByGuid(String guid) {
        return accountDao.getAccount(guid);
    }

    @Override
    public Account getRootAccount() {
        return accountDao.getRootAccount();
    }

    @Override
    public void addAccountTotalsToTree(Account rootAccount) {
        List<AccountSum> sums = accountDao.getAccountTotals();
        AccountHelper.addSumsToAccountTree(rootAccount, sums);
    }

    @Override
    public List<AccountSum> getStats(Account account) {
        List<Account> siblings = AccountHelper.getAllSiblings(account);
        List<AccountSum> rawSums = accountDao.getAccountTotalsMonthlyInterval(siblings);
        return rawSums;
    }


    @Override
    public List<List<AccountSum>> getStats(List<Account> accounts) {
        List<List<AccountSum>> ret = new ArrayList<List<AccountSum>>();
        for(Account account : accounts) {
            ret.add(getStats(account));
        }
        return ret;
    }

    @Override
    public List<AccountSum> getStatsTotal(List<Account> accounts) {
        List<Account> siblings = new ArrayList<Account>();
        for(Account account : accounts) {
            siblings.addAll(AccountHelper.getAllSiblings(account));
        }
        return accountDao.getAccountTotalsMonthlyInterval(siblings);
    }

    @Override
    public List<AccountSum> getStatsTotalWithoutSiblings(List<Account> accounts) {
        return accountDao.getAccountTotalsMonthlyInterval(accounts);
    }

    @Override
    public List<AccountSum> getStatsTotalWithoutSiblings(List<Account> accounts, String description) {
        return accountDao.getAccountTotalsMonthlyIntervalDesc(accounts, description);
    }

    @Override
    public List<Account> getAccounts(List<String> accountGuids) {
        List<Account> ret = new ArrayList<Account>();
        for(String guid : accountGuids) {
            ret.add(findByGuid(guid));
        }
        return ret;
    }


}
