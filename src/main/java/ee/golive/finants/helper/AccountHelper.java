package ee.golive.finants.helper;

import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;

import java.util.ArrayList;
import java.util.List;

public class AccountHelper {

    public static void addSumsToAccountTree(Account root, List<AccountSum> sums) {
        addSumToAccount(root, sums);
    }

    public static void addSumToAccount(Account parent, List<AccountSum> sums) {
        Long sum = 0l;
        if (!parent.getChildAccounts().isEmpty()) {
            for(Account child : parent.getChildAccounts()) {
                addSumToAccount(child, sums);
                sum+=child.getSum();
            }
        }
        AccountSum sumObj = findCorrespondingAccountSum(parent, sums);
        sum+=sumObj.getSum();
        parent.setSum(sum);
    }

    public static AccountSum findCorrespondingAccountSum(Account account, List<AccountSum> sums) {
        AccountSum correspondingSum = new AccountSum();
        for(AccountSum item : sums) {
            if (item.getAccount_guid().equals(account.getGuid())) {
                correspondingSum = item;
                sums.remove(item);
                break;
            }
        }
        return correspondingSum;
    }

    public static List<Account> getAllSiblings(Account account) {
        List<Account> siblings = new ArrayList<Account>();
        return getAllSiblings(account, siblings);
    }

    public static List<Account> getAllSiblings(Account account, List<Account> list) {
        list.add(account);
        if (!account.getChildAccounts().isEmpty()) {
            for(Account child : account.getChildAccounts()) {
                list = getAllSiblings(child, list);
            }
        }
        return list;
    }
}
