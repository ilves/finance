package ee.golive.finants.controller;

import ee.golive.finants.model.Account;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping("/account/{someID}")
    public String accountTransactions(Model model, @PathVariable(value="someID") String guid) {
        Account account = accountService.findByGuid(guid);
        model.addAttribute(account);
        return "account";
    }
}
