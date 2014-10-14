package ee.golive.finants.controller;

import com.google.gson.Gson;
import ee.golive.finants.chart.BarChart;
import ee.golive.finants.chart.Graph;
import ee.golive.finants.chart.LineChart;
import ee.golive.finants.chart.StackedBarChart;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.helper.CollectionsHelper;
import ee.golive.finants.helper.FinanceHelper;
import ee.golive.finants.menu.Menu;
import ee.golive.finants.menu.MenuService;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Series;
import ee.golive.finants.report.P2PReport;
import ee.golive.finants.report.Report;
import ee.golive.finants.services.AccountService;
import in.satpathy.financial.XIRR;
import in.satpathy.financial.XIRRData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReportController {

    @Autowired
    public AccountService accountService;

    @Autowired
    public CollectionsHelper collectionsHelper;

    @Autowired
    public MenuService menuService;

    @RequestMapping("/p2p")
    public String test(Model model, HttpServletRequest request) {
        Report report = new P2PReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/future")
    public String future(Model model, HttpServletRequest request) {

        int years = 10;

        double interest = 0.12;
        double infla = 0.0418;


        double needed = 800*1.23;
        double amount = needed*12/interest;

        System.out.println(amount);
        System.out.println(FinanceHelper.fv(amount, years*12, infla/12));
        System.out.println(FinanceHelper.pmt(interest/12, years*12, 0, amount));

        return "report";
    }

    @RequestMapping("/portfolio")
    public String portfolio(Model model, HttpServletRequest request) {
        setMenuActive("portfolio");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);
        List<Calendar> syncInterval = ChartHelper.getIntervalList(getDate("start", "full"), getDate("end", "full"));

        List<Account> cashAccounts = collectionsHelper.getByName("cash");
        List<AccountSum> cashSum = ChartHelper.sync(accountService.getStatsTotal(cashAccounts, step), syncInterval, step);
        AccountHelper.addInSeries(cashSum);
        cashSum = ChartHelper.sync(cashSum, interval, step);

        List<Account> realestateAccounts = collectionsHelper.getByName("realEstate");
        List<AccountSum> realSum = ChartHelper.sync(accountService.getStatsTotal(realestateAccounts, step), syncInterval, step);
        AccountHelper.addInSeries(realSum);
        realSum = ChartHelper.sync(realSum, interval, step);

        List<Account> loanAccounts = collectionsHelper.getByName("loan");
        List<AccountSum> loanSum = ChartHelper.sync(accountService.getStatsTotal(loanAccounts, step), syncInterval, step);
        AccountHelper.addInSeries(loanSum);
        loanSum = ChartHelper.sync(loanSum, interval, step);

        List<Account> sharesAccounts = collectionsHelper.getByName("shares");
        List<AccountSum> shareSum = ChartHelper.sync(accountService.getStatsTotal(sharesAccounts, step), syncInterval, step);
        AccountHelper.addInSeries(shareSum);
        shareSum = ChartHelper.sync(shareSum, interval, step);

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
        model.addAttribute("step", step);

        return "report";
    }

    @RequestMapping("/investing")
    public String investing(Model model, HttpServletRequest request) {
        setMenuActive("investing");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);

        List<Account> loanAccounts = collectionsHelper.getByName("loanInvestment");
        List<AccountSum> loanSum = ChartHelper.sync(accountService.getStatsTotalWithoutSiblings(loanAccounts, step, "Sissekanne"), interval, step);

        List<Account> realestateAccounts = collectionsHelper.getByName("realEstate");
        List<AccountSum> realSum = ChartHelper.sync(accountService.getStatsTotal(realestateAccounts, step), interval, step);

        List<Account> sharesAccounts = collectionsHelper.getByName("shares");
        List<AccountSum> shareSum = ChartHelper.sync(accountService.getStatsTotal(sharesAccounts, step), interval, step);

        List<Series> series = new ArrayList<Series>();
        series.add(new Series("P2P LOAN", AccountHelper.transformAccountSum(loanSum)));
        series.add(new Series("REALESTATE", AccountHelper.transformAccountSum(realSum)));
        series.add(new Series("SHARES", AccountHelper.transformAccountSum(shareSum)));

        Graph investments = new StackedBarChart();
        investments.setSeries(series);
        investments.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(investments));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        model.addAttribute("p2p_sum", AccountHelper.sumList(loanSum));
        model.addAttribute("real_sum", AccountHelper.sumList(realSum));
        model.addAttribute("shares_sum", AccountHelper.sumList(shareSum));
        model.addAttribute("months", interval.size());

        return "report_investing";
    }

    @RequestMapping("/income")
    public String income(Model model, HttpServletRequest request) {
        setMenuActive("income");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);

        List<Account> personal = collectionsHelper.getByName("personalIncome");
        List<AccountSum> personalSum = ChartHelper.sync(accountService.getStatsTotal(personal, step), interval, step);

        List<Account> company = collectionsHelper.getByName("companyIncome");
        List<AccountSum> companySum = ChartHelper.sync(accountService.getStatsTotal(company, step), interval, step);

        List<Account> bondora = collectionsHelper.getByName("p2pincome");
        List<AccountSum> bondoraSum = ChartHelper.sync(accountService.getStatsTotal(bondora, step), interval, step);

        List<Series> series = new ArrayList<Series>();
        series.add(new Series("Salary", AccountHelper.transformAccountSum(personalSum)));
        series.add(new Series("Company", AccountHelper.transformAccountSum(companySum)));
        series.add(new Series("Interests", AccountHelper.transformAccountSum(bondoraSum)));

        Graph investments = new StackedBarChart();
        investments.setSeries(series);
        investments.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(investments));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        model.addAttribute("salary_sum", AccountHelper.sumList(personalSum));
        model.addAttribute("company_sum", AccountHelper.sumList(companySum));
        model.addAttribute("months", interval.size());

        return "report_income";
    }

    @RequestMapping("/networth")
    public String networth(Model model, HttpServletRequest request) {
        setMenuActive("networth");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);
        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);
        List<Calendar> syncInterval = ChartHelper.getIntervalList(getDate("start", "full"), getDate("end", "full"));

        List<AccountSum> assets = ChartHelper.sync(
                accountService.getStatsTotal(collectionsHelper.getByName("assets"), step), syncInterval
        );
        AccountHelper.addInSeries(assets);
        assets = ChartHelper.sync(assets, interval, step);

        List<AccountSum> liab = ChartHelper.sync(
                accountService.getStatsTotal(collectionsHelper.getByName("liabilities"), step), syncInterval
        );
        AccountHelper.addInSeries(liab);
        liab = ChartHelper.sync(liab, interval, step);


        List<Series> series = new ArrayList<Series>();
        series.add(new Series("Assets", AccountHelper.transformAccountSum(assets), "column"));
        series.add(new Series("Liabilities", AccountHelper.transformAccountSum(liab, true), "column"));
        series.add(new Series("Networth", AccountHelper.transformAccountDifference(assets, liab), "line"));

        Graph net = new BarChart();
        net.setSeries(series);
        net.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(net));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        return "report";
    }

    @RequestMapping("/expenses")
    public String expenses(Model model, HttpServletRequest request) {
        setMenuActive("expenses");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);

        List<Account> personal = collectionsHelper.getByName("expenses");
        List<Account> company = collectionsHelper.getByName("expensesCompany");

        List<AccountSum> personalSum = ChartHelper.sync(accountService.getStatsTotal(personal, step), interval, step);
        List<AccountSum> companySum = ChartHelper.sync(accountService.getStatsTotal(company, step), interval, step);

        List<Series> series = new ArrayList<Series>();
        series.add(new Series("Expenses Personal", AccountHelper.transformAccountSum(personalSum)));
        series.add(new Series("Expenses Company", AccountHelper.transformAccountSum(companySum)));

        Graph investments = new StackedBarChart();
        investments.setSeries(series);
        investments.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(investments));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        model.addAttribute("salary_sum", AccountHelper.sumList(personalSum));
        model.addAttribute("months", interval.size());

        return "report";
    }

    @RequestMapping("/savings")
    public String savings(Model model, HttpServletRequest request) {
        setMenuActive("savings");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);

        List<Account> exp = collectionsHelper.getByName("expenses");
        List<Account> inc = collectionsHelper.getByName("personalIncome");

        if (true) {
            exp.addAll(collectionsHelper.getByName("expensesCompany"));
            inc.addAll(collectionsHelper.getByName("companyIncome"));
        }

        List<AccountSum> expSum = ChartHelper.sync(accountService.getStatsTotal(exp, step), interval, step);
        List<AccountSum> incSum = ChartHelper.sync(accountService.getStatsTotal(inc, step), interval, step);




        List<Series> series = new ArrayList<Series>();
        series.add(new Series("Savings", AccountHelper.transformAccountSum(
                ChartHelper.difference(incSum, expSum)
        )));

        List<Series> series2 = new ArrayList<Series>();
        series2.add(new Series("Savings rate", ChartHelper.differencePrecent(incSum, expSum)));
        series2.add(new Series("Savings rate sliding", ChartHelper.differencePrecentSliding(incSum, expSum)));

        Graph save = new StackedBarChart();
        save.setSeries(series);
        save.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        Graph line = new LineChart();
        line.setSeries(series2);
        line.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(line));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        model.addAttribute("months", interval.size());

        return "report";
    }

    @RequestMapping("/xirr")
    public String xirr(Model model, HttpServletRequest request) {

        String step = getStep(request);
        Date start = parseDate("2010/12");
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);

        List<Account> in = collectionsHelper.getByName("loanInvestment");
        List<AccountSum> inSum = accountService.getStatsTotalWithoutSiblings(in, step, "Sissekanne");

        List<Account> out = collectionsHelper.getByName("loanInvestment");
        List<AccountSum> outSeries = ChartHelper.sync(accountService.getStatsTotal(out, step), interval, step);


        AccountHelper.addInSeries(outSeries);

        List<Account> red = collectionsHelper.getByName("loanRed");
        List<AccountSum> redSeries = ChartHelper.sync(accountService.getStatsTotal(red, step), interval, step);
        AccountHelper.addInSeries(redSeries);



        List<Series> series = new ArrayList<Series>();
        series.add(new Series("Optimistic",  AccountHelper.calculateXIRRSeries(inSum, outSeries, interval)));
        series.add(new Series("Neutral",  AccountHelper.calculateXIRRSeries(inSum, ChartHelper.difference(outSeries, redSeries), interval)));

        Graph line = new LineChart();
        line.setSeries(series);
        line.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(line));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        model.addAttribute("months", interval.size());

        return "report";
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("y/M").parse(date);
        } catch (ParseException e) {

        }
        return new Date();
    }

    public Date getDate(String type, HttpServletRequest request) {
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
                start = parseDate("2010/12");
        }
        return type.equals("start") ? start : end;
    }

    public String getPeriod(HttpServletRequest request) {
        return request.getParameter("period") == null ? "current" : request.getParameter("period");
    }

    public String getStep(HttpServletRequest request) {
        return request.getParameter("step") == null ? "month" : request.getParameter("step");
    }

    public void setMenuActive(String key) {
        Menu menu = menuService.getMenu("sidebar");
        menu.setActive(key);
    }
}
