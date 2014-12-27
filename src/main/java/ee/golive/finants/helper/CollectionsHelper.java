package ee.golive.finants.helper;

import ee.golive.finants.model.Account;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionsHelper {

    @Autowired
    AccountService accountService;

    @Autowired
    Environment env;

    public List<Account> getByName(String name) {
        return accountService.getAccounts(getAccounts(name));
    }

    private List<String> getAccounts(String name) {
        String rawList = env.getProperty("account." + name);
        List<String> accounts = new ArrayList<String>();
        for (String accountId : rawList.split(",")) {
            if (accountId.contains("account.")) {
                accounts.addAll(getAccounts(accountId.trim().replace("account.","")));
            } else {
                accounts.add(accountId.trim());
            }
        }
        return accounts;
    }
}
