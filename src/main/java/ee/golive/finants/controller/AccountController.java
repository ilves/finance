package ee.golive.finants.controller;

import com.google.gson.Gson;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Series;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping("/account/{someID}")
    public String accountTransactions(Model model, @PathVariable(value="someID") String guid) {
        Account account = accountService.findByGuid(guid);
        model.addAttribute(account);
        model.addAttribute("stats", accountService.getStats(account));
        model.addAttribute("total", 0);

        List<Series> series = new ArrayList<Series>();
        List<Float> list1 = ChartHelper.transformAccountSum(accountService.getStats(account));

        series.add(new Series("Test", list1));

        SimpleDateFormat sdfDate = new SimpleDateFormat("y/M");
        List<String> categories = new ArrayList<String>();

        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat("y/M").parse("2012/12");
            end = new SimpleDateFormat("y/M").parse("2014/08");
            System.out.println(start);
            System.out.println(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         /**
        for(Date tmp : ChartHelper.getIntervalList(start, end)) {
            categories.add(sdfDate.format(tmp).toString());
        }
            **/
        model.addAttribute("chartseries", new Gson().toJson(series));
        model.addAttribute("cartcategories", new Gson().toJson(categories));

        return "account";
    }
}
