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
    public List<AccountSum> getStats(Account account, String step) {
        List<Account> siblings = AccountHelper.getAllSiblings(account);
        List<AccountSum> rawSums =
                step.equals("year") ?
                        accountDao.getAccountTotalsYearlyInterval(siblings) :
                        accountDao.getAccountTotalsMonthlyInterval(siblings);
        return rawSums;
    }


    public List<AccountSum> getStats(Account account, String step, String description) {
        List<Account> siblings = AccountHelper.getAllSiblings(account);
        List<AccountSum> rawSums =
                step.equals("year") ?
                        accountDao.getAccountTotalsYearlyIntervalDesc(siblings, description) :
                        accountDao.getAccountTotalsMonthlyIntervalDesc(siblings, description);
        return rawSums;
    }

    @Override
    public List<List<AccountSum>> getStats(List<Account> accounts, String step) {
        List<List<AccountSum>> ret = new ArrayList<List<AccountSum>>();
        for(Account account : accounts) {
            ret.add(getStats(account, step));
        }
        return ret;
    }

    @Override
    public List<List<AccountSum>> getStats(List<Account> accounts, String step, String description) {
        List<List<AccountSum>> ret = new ArrayList<List<AccountSum>>();
        for(Account account : accounts) {
            ret.add(getStats(account, step, description));
        }
        return ret;
    }

    @Override
    public List<AccountSum> getStatsTotal(List<Account> accounts, String step) {
        List<Account> siblings = new ArrayList<Account>();
        for(Account account : accounts) {
            siblings.addAll(AccountHelper.getAllSiblings(account));
        }
        if (step.equals("year")) {
            return accountDao.getAccountTotalsYearlyInterval(siblings);
        } else {
            return accountDao.getAccountTotalsMonthlyInterval(siblings);
        }
    }


    @Override
    public List<AccountSum> getStatsTotalWithoutSiblings(List<Account> accounts, String step, String description) {
        if (step.equals("year")) {
            return accountDao.getAccountTotalsYearlyIntervalDesc(accounts, description);
        } else {
            return accountDao.getAccountTotalsMonthlyIntervalDesc(accounts, description);
        }
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
