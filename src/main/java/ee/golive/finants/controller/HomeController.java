package ee.golive.finants.controller;

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

    @RequestMapping("/")
    public String test(Model model) {
        Account root = accountService.getRootAccount();
        model.addAttribute("root", root);
        model.addAttribute("accountService", accountService);

        return "home";
    }
}
