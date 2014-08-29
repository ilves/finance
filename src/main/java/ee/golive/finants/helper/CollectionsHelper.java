package ee.golive.finants.helper;

import ee.golive.finants.model.Account;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionsHelper {

    @Autowired
    AccountService accountService;

    public List<Account> getLoanAccounts() {
        List<String> loans = new ArrayList<String>();
        loans.add("89ff4bac37f35519f5c6dfcac259c3a5");
        loans.add("250a0f15185a8732945626e9cf8fe811");
        CollectionsHelper helper = new CollectionsHelper();
        return accountService.getAccounts(loans);
    }

    public List<Account> getLoanInvestmentAccounts() {
        List<String> loans = new ArrayList<String>();
        loans.add("d85b1187a4612cfe9e4f5e498e548095");
        loans.add("bf93b0393049681fb901f48c7e942826");
        CollectionsHelper helper = new CollectionsHelper();
        return accountService.getAccounts(loans);
    }

    public List<Account> getRealestateAccounts() {
        List<String> realestate = new ArrayList<String>();
        realestate.add("c249ae656b08cf1928e5e27f551f0078");
        CollectionsHelper helper = new CollectionsHelper();
        return accountService.getAccounts(realestate);
    }

    public List<Account> getSharesAccounts() {
        List<String> shares = new ArrayList<String>();
        shares.add("9c958dc7cd28a9a8bd8feabf669b579d");
        shares.add("0433f131e1e6dc0683795b08029f639c");
        CollectionsHelper helper = new CollectionsHelper();
        return accountService.getAccounts(shares);
    }

    public List<Account> getCashAccounts() {
        List<String> cash = new ArrayList<String>();
        cash.add("4bb5c2e0a5afa049647ed13f3c4111dd");
        cash.add("1e4fb0b4498c8c67e1dbff500307d5ff");
        CollectionsHelper helper = new CollectionsHelper();
        return accountService.getAccounts(cash);
    }

    public List<Account> getBondoraIncomeAccounts() {
        List<String> accountGuids = new ArrayList<String>();
        accountGuids.add("ecaf49a62e16e6dc309f1b0bb7d2ea0f");
        accountGuids.add("1e0af2b7bc5b6e07254828738587ad28");
        accountGuids.add("65681ce9156cc06d6ba22ff27225f504");
        accountGuids.add("ed4bb6ad5fb74362e1d807a040de0b64");
        return accountService.getAccounts(accountGuids);
    }

    public List<Account> getCompanyIncome() {
        List<String> accountGuids = new ArrayList<String>();
        accountGuids.add("c210ff96cc04deadbba96c87b424f74a");
        return accountService.getAccounts(accountGuids);
    }

    public List<Account> getPersonalIncome() {
        List<String> accountGuids = new ArrayList<String>();
        accountGuids.add("9884aea0e875171362cfcc30e77bc459");
        return accountService.getAccounts(accountGuids);
    }
}
