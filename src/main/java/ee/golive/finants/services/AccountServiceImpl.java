package ee.golive.finants.services;

import ee.golive.finants.dao.AccountDao;
import ee.golive.finants.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return accountDao.findByGuid(guid);
    }

    @Override
    public Account getRootAccount() {
        return accountDao.getRootAccount();
    }

    @Override
    public double getAccountTotal(Account account) {
        Double sum = 0.0;
        if (!account.getChildAccounts().isEmpty()) {
            sum+=getChildrenTotal(account);
        }
        return accountDao.getAccountTotal(account);
    }

    private double getChildrenTotal(Account account) {
        Double sum = 0.0;
        for(Account acc : account.getChildAccounts()) {
            sum+=getAccountTotal(acc);
        }
        return sum;
    }
}
