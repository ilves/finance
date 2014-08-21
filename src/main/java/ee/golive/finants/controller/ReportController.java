package ee.golive.finants.controller;

import ee.golive.finants.chart.Chart;
import ee.golive.finants.chart.StackedBarChart;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Series;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    AccountService accountService;

    @RequestMapping("/bondora")
    public String test(Model model) {
        List<String> accountGuids = new ArrayList<String>();

        accountGuids.add("ecaf49a62e16e6dc309f1b0bb7d2ea0f");
        accountGuids.add("1e0af2b7bc5b6e07254828738587ad28");
        accountGuids.add("65681ce9156cc06d6ba22ff27225f504");
        accountGuids.add("ed4bb6ad5fb74362e1d807a040de0b64");

        List<Account> accounts = accountService.getAccounts(accountGuids);
        List<List<AccountSum>> sums = accountService.getStats(accounts);

        System.out.println(sums);

        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat("y/M").parse("2012/12");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Calendar> interval = ChartHelper.getIntervalList(start, end);

        List<Series> series = new ArrayList<Series>();
        for(List<AccountSum> sum : sums) {
            series.add(
                    new Series("SERIES",
                            ChartHelper.transformAccountSum(ChartHelper.sync(sum, interval))));
        }

        Chart income = new StackedBarChart();
        income.setSeries(series);
        income.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", income);

        return "report";
    }
}
