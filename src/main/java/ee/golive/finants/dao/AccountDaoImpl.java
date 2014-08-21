package ee.golive.finants.dao;

import ee.golive.finants.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Account findByGuid(String guid) {
        Account account = (Account) this.entityManager.find(Account.class, guid);
        return account;
    }

    @Override
    @Transactional
    public List<Account> getList() {
        List<Account> accountList = this.entityManager.createQuery("from Account").getResultList();
        return accountList;
    }

    @Override
    @Transactional
    public Account getRootAccount() {
        Account account = (Account) this.entityManager.createQuery(
                "from Account where account_type = 'ROOT' AND name = 'Root Account'").getSingleResult();
        List<Account> accountList = this.entityManager.createQuery("from Account").getResultList();
        this.entityManager.createNamedQuery("fullAccountTree").getResultList();
        return this.entityManager.find(Account.class, account.getGuid());
    }

    @Override
    @Transactional
    public Double getAccountTotal(Account account) {
        Double sum = 0.0;
        if (!account.getChildAccounts().isEmpty()) {
            for (Account child : account.getChildAccounts()) {
                sum+=getAccountTotal(child);
            }
        }
        Long sumInt = (Long)this.entityManager.createQuery("select sum(value_num) FROM Split where account_guid = :aq")
                .setParameter("aq", account.getGuid()).getSingleResult();
        if (sumInt != null) {
            sum+= Double.valueOf(sumInt);
        }
        account.setSum(sum);
        return account.getSum();
    }
}