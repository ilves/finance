package ee.golive.finants.controller;

import ee.golive.finants.dao.AccountDao;
import ee.golive.finants.model.Account;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountDao accountDao;

    @RequestMapping("/accounts")
    public String test(Model model) {
        Account root = accountService.getRootAccount();
        accountService.addAccountTotalsToTree(root);
        model.addAttribute("root", root);
        model.addAttribute("accountService", accountService);

        return "home";
    }

    @RequestMapping("/")
    public String dashboard(Model model) {

        Long assets = accountDao.getAccountTotal(accountService.findById("assets"));
        Long liabilities = accountDao.getAccountTotal(accountService.findById("liabilities"));

        model.addAttribute("cashTotal", accountDao.getAccountTotal(accountService.findById("money")));
        model.addAttribute("netWorth", assets+liabilities);
        model.addAttribute("assetsTotal", assets);
        model.addAttribute("liabilitiesTotal", liabilities);
        model.addAttribute("investmentsTotal", accountDao.getAccountTotal(accountService.findById("investments_all")));

        return "dashboard";
    }
}
