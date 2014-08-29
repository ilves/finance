package ee.golive.finants.controller;

import com.google.gson.Gson;
import ee.golive.finants.chart.Graph;
import ee.golive.finants.chart.StackedBarChart;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.helper.CollectionsHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Series;
import ee.golive.finants.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    CollectionsHelper collectionsHelper;

    @RequestMapping("/bondora")
    public String test(Model model, HttpServletRequest request) {

        List<Account> accounts = collectionsHelper.getBondoraIncomeAccounts();
        List<List<AccountSum>> sums = accountService.getStats(accounts);

        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end);

        List<Series> series = new ArrayList<Series>();
        for(List<AccountSum> sum : sums) {
            series.add(
                    new Series(accountService.findByGuid(sum.get(0).getAccount_guid()).getName(),
                            AccountHelper.transformAccountSum(ChartHelper.sync(sum, interval))));
        }

        Graph income = new StackedBarChart();
        income.setSeries(series);
        income.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(income));
        model.addAttribute("period", getPeriod(request));

        return "report";
    }

    @RequestMapping("/portfolio")
    public String portfolio(Model model, HttpServletRequest request) {

        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end);
        List<Calendar> syncInterval = ChartHelper.getIntervalList(getDate("start", "full"), getDate("end", "full"));

        List<Account> cashAccounts = collectionsHelper.getCashAccounts();
        List<AccountSum> cashSum = ChartHelper.sync(accountService.getStatsTotal(cashAccounts), syncInterval);
        System.out.println(cashSum);
        AccountHelper.addInSeries(cashSum);
        cashSum = ChartHelper.sync(cashSum, interval);

        List<Account> realestateAccounts = collectionsHelper.getRealestateAccounts();
        List<AccountSum> realSum = ChartHelper.sync(accountService.getStatsTotal(realestateAccounts), syncInterval);
        AccountHelper.addInSeries(realSum);
        realSum = ChartHelper.sync(realSum, interval);

        List<Account> loanAccounts = collectionsHelper.getLoanAccounts();
        List<AccountSum> loanSum = ChartHelper.sync(accountService.getStatsTotal(loanAccounts), syncInterval);
        AccountHelper.addInSeries(loanSum);
        loanSum = ChartHelper.sync(loanSum, interval);

        List<Account> sharesAccounts = collectionsHelper.getSharesAccounts();
        List<AccountSum> shareSum = ChartHelper.sync(accountService.getStatsTotal(sharesAccounts), syncInterval);
        AccountHelper.addInSeries(shareSum);
        shareSum = ChartHelper.sync(shareSum, interval);

        List<Series> series = new ArrayList<Series>();
        series.add(new Series("CASH", AccountHelper.transformAccountSum(cashSum)));
        series.add(new Series("P2P LOAN", AccountHelper.transformAccountSum(loanSum)));
        series.add(new Series("REALESTATE", AccountHelper.transformAccountSum(realSum)));
        series.add(new Series("SHARES", AccountHelper.transformAccountSum(shareSum)));


        StackedBarChart portfolio = new StackedBarChart();
        portfolio.setSeries(series);
        portfolio.setStacking("percent");
        portfolio.tooltip.pointFormat = "<span style=\"color:{series.color}\">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>";
        portfolio.tooltip.shared = true;
        portfolio.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(portfolio));
        model.addAttribute("period", getPeriod(request));

        return "report";
    }

    @RequestMapping("/investing")
    public String investing(Model model, HttpServletRequest request) {

        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end);

        List<Account> loanAccounts = collectionsHelper.getLoanInvestmentAccounts();
        List<AccountSum> loanSum = ChartHelper.sync(accountService.getStatsTotalWithoutSiblings(loanAccounts, "Sissekanne"), interval);

        List<Account> realestateAccounts = collectionsHelper.getRealestateAccounts();
        List<AccountSum> realSum = ChartHelper.sync(accountService.getStatsTotal(realestateAccounts), interval);

        List<Account> sharesAccounts = collectionsHelper.getSharesAccounts();
        List<AccountSum> shareSum = ChartHelper.sync(accountService.getStatsTotal(sharesAccounts), interval);

        List<Series> series = new ArrayList<Series>();
        series.add(new Series("P2P LOAN", AccountHelper.transformAccountSum(loanSum)));
        series.add(new Series("REALESTATE", AccountHelper.transformAccountSum(realSum)));
        series.add(new Series("SHARES", AccountHelper.transformAccountSum(shareSum)));

        Graph investments = new StackedBarChart();
        investments.setSeries(series);
        investments.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(investments));
        model.addAttribute("period", getPeriod(request));

        model.addAttribute("p2p_sum", AccountHelper.sumList(loanSum));
        model.addAttribute("real_sum", AccountHelper.sumList(realSum));
        model.addAttribute("shares_sum", AccountHelper.sumList(shareSum));
        model.addAttribute("months", interval.size());

        return "report_investing";
    }

    @RequestMapping("/income")
    public String income(Model model, HttpServletRequest request) {

        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end);

        List<Account> personal = collectionsHelper.getPersonalIncome();
        List<AccountSum> personalSum = ChartHelper.sync(accountService.getStatsTotal(personal), interval);

        List<Account> company = collectionsHelper.getCompanyIncome();
        List<AccountSum> companySum = ChartHelper.sync(accountService.getStatsTotal(company), interval);

        List<Series> series = new ArrayList<Series>();
        series.add(new Series("Salary", AccountHelper.transformAccountSum(personalSum)));
        series.add(new Series("Company", AccountHelper.transformAccountSum(companySum)));

        Graph investments = new StackedBarChart();
        investments.setSeries(series);
        investments.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(investments));
        model.addAttribute("period", getPeriod(request));

        model.addAttribute("salary_sum", AccountHelper.sumList(personalSum));
        model.addAttribute("company_sum", AccountHelper.sumList(companySum));
        model.addAttribute("months", interval.size());

        return "report_income";
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("y/M").parse(date);
        } catch (ParseException e) {

        }
        return new Date();
    }

    private Date getDate(String type, HttpServletRequest request) {
        return getDate(type, getPeriod(request));
    }

    private Date getDate(String type, String period) {
        Date start = new Date();
        Date end = new Date();
        Calendar cl = Calendar.getInstance();
        switch (period) {
            case "current":
                cl.set(Calendar.YEAR, cl.getWeekYear());
                cl.set(Calendar.MONTH, 0);
                start = cl.getTime();
                break;
            case "previous":
                cl.set(Calendar.YEAR, cl.getWeekYear()-1);
                cl.set(Calendar.MONTH, 0);
                start = (Date)cl.getTime().clone();
                cl.set(Calendar.MONTH, 11);
                end = cl.getTime();
                break;
            default:
                start = parseDate("2012/12");
        }
        return type.equals("start") ? start : end;
    }

    private String getPeriod(HttpServletRequest request) {
        return request.getParameter("period") == null ? "current" : request.getParameter("period");
    }
}
