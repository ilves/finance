package ee.golive.finants.controller;

import com.google.gson.Gson;
import ee.golive.finants.chart.*;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.helper.CollectionsHelper;
import ee.golive.finants.helper.FinanceHelper;
import ee.golive.finants.menu.Menu;
import ee.golive.finants.menu.MenuService;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.chart.Series;
import ee.golive.finants.report.*;
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
    public AccountService accountService;

    @Autowired
    public CollectionsHelper collectionsHelper;

    @Autowired
    public MenuService menuService;

    @RequestMapping("/networth")
    public String networth(Model model, HttpServletRequest request) {
        Report report = new NetworthReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/p2p")
    public String test(Model model, HttpServletRequest request) {
        Report report = new P2PReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/portfolio")
    public String portfolio2(Model model, HttpServletRequest request) {
        Report report = new PortfolioReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/savings")
    public String savings(Model model, HttpServletRequest request) {
        Report report = new SavingsReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/income")
    public String income(Model model, HttpServletRequest request) {
        Report report = new IncomeReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/investing")
    public String investing(Model model, HttpServletRequest request) {
        Report report = new InvestmentReport(this, request, model);
        return report.getTemplate();
    }

    @RequestMapping("/expenses")
    public String expenses(Model model, HttpServletRequest request) {
        Report report = new ExpensesReport(this, request, model);
        return report.getTemplate();
    }





    @RequestMapping("/future")
    public String future(Model model, HttpServletRequest request) {

        int years = 10;

        double interest = 0.10;
        double infla = 0.0418;

        String step = "month";
        List<Calendar> interval = ChartHelper.getIntervalList(parseDate("2014/01/01"), parseDate("2024/01/01"), step);
        List<Calendar> syncinterval = ChartHelper.getIntervalList(parseDate("2010/01/01"), parseDate("2024/01/01"), step);
        List<Calendar> intervalToday = ChartHelper.getIntervalList(parseDate("2014/01/01"), new Date(), step);

        List<AccountSum> assets = ChartHelper.sync(
                accountService.getStatsTotal(collectionsHelper.getByName("assets"), step), syncinterval
        );
        AccountHelper.addInSeries(assets);
        assets = ChartHelper.sync(assets, interval, step);

        List<AccountSum> liab = ChartHelper.sync(
                accountService.getStatsTotal(collectionsHelper.getByName("liabilities"), step), syncinterval
        );
        AccountHelper.addInSeries(liab);
        liab = ChartHelper.sync(liab, interval, step);

        List<Account> invAcc = collectionsHelper.getByName("realEstate");
        invAcc.addAll(collectionsHelper.getByName("loan"));
        invAcc.addAll(collectionsHelper.getByName("shares"));
        invAcc.addAll(collectionsHelper.getByName("indexfunds"));

        List<Account> liqAcc = collectionsHelper.getByName("cash");
        liqAcc.addAll(invAcc);

        List<AccountSum> liqAccSum = ChartHelper.sync(accountService.getStatsTotal(liqAcc, step), syncinterval, step);
        AccountHelper.addInSeries(liqAccSum);
        liqAccSum = ChartHelper.sync(liqAccSum, intervalToday, step);

        List<AccountSum> invAccSum = ChartHelper.sync(accountService.getStatsTotal(invAcc, step), syncinterval, step);
        AccountHelper.addInSeries(invAccSum);
        invAccSum = ChartHelper.sync(invAccSum, intervalToday, step);




        Calendar cl = Calendar.getInstance();



        List<Series> series = new ArrayList<>();

        addPrediction(interest, infla, years, 500, series, "LV1");
        addPrediction(interest, infla, years, 800, series, "LV2");
        addPrediction(interest, infla, years, 1600, series, "LV3");


        cl = Calendar.getInstance();
        cl.setTime(parseDate("2014/02/31"));

        //series.add(new PointSeries("NETWORTH", AccountHelper.transformAccountDifference(assets, liab, cl, Calendar.MONTH)));

        cl = Calendar.getInstance();
        cl.setTime(parseDate("2014/02/31"));
        //series.add(new PointSeries("LIQ", AccountHelper.transformAccountSum(liqAccSum, cl, Calendar.MONTH)));

        cl = Calendar.getInstance();
        cl.setTime(parseDate("2014/02/31"));
        series.add(new PointSeries("INV", AccountHelper.transformAccountSum(invAccSum, cl, Calendar.MONTH)));




        List<Point> data2 = new ArrayList<>();


        AccountSum lastInv = invAccSum.get(invAccSum.size()-1);
        Calendar cl2 = Calendar.getInstance();
        cl2.setTime(parseDate(lastInv.getYear()+"/"+lastInv.getMonth()+"/28"));
        cl2.add(Calendar.MONTH, 1);
        double last = lastInv.getSumMoney();

        for(long m = lastInv.getMonth(); m <= years*12; m++) {
            last = FinanceHelper.fv(last, 1, interest/12);
            data2.add(new Point(cl2.getTime().getTime(), (float) last));
            cl2.add(Calendar.MONTH, 1);
        }

        Series investmentGrowth = new PointSeries("Investments Growth", data2);
        investmentGrowth.pointStart = parseDate("2014/02/31").getTime();
        series.add(investmentGrowth);


        LineChart stepsg = new LineChart();
        stepsg.disableDots();
        stepsg.setSeries(series);
        stepsg.setDateTime();
        stepsg.setZoomChart();


        model.addAttribute("chart", new Gson().toJson(stepsg));

        return "report";
    }

    public void addPrediction(double interest, double infla, int years, double needed, List<Series> series, String name) {

        double amount = needed*12/interest;

        amount = FinanceHelper.fv(amount, years*12, infla/12);

        double steps = FinanceHelper.pmt(interest/12, years*12, 0, amount);
        double last = 0;
        List<Point> data = new ArrayList<>();

        Calendar cl = Calendar.getInstance();
        cl.setTime(parseDate("2014/02/31"));


        for(int n = 1; n <= years*12; n++) {
            last = FinanceHelper.fv(last, 1, interest/12) + steps;
            data.add(new Point(cl.getTime().getTime(), (float) last));
            cl.add(Calendar.MONTH, 1);
        }


        Series series1 = new PointSeries(name, data);
        series1.pointStart = parseDate("2014/02/31").getTime();
        series.add(series1);
    }




    @RequestMapping("/income-old")
    public String incomeOld(Model model, HttpServletRequest request) {
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

        List<Account> adv = collectionsHelper.getByName("adv");
        List<AccountSum> advSum = ChartHelper.sync(accountService.getStatsTotal(adv, step), interval, step);

        List<Series> series = new ArrayList<Series>();
        series.add(new NormalSeries("Salary", AccountHelper.transformAccountSum(personalSum)));
        series.add(new NormalSeries("Company", AccountHelper.transformAccountSum(companySum)));
        series.add(new NormalSeries("Interests", AccountHelper.transformAccountSum(bondoraSum)));
        series.add(new NormalSeries("Advertisement", AccountHelper.transformAccountSum(advSum)));

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

    @RequestMapping("/networth-old")
    public String networthOld(Model model, HttpServletRequest request) {
        setMenuActive("networth");

        String step = getStep(request);
        Date start = getDate("start", request);
        Date end = getDate("end", request);
        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);
        List<Calendar> syncInterval = ChartHelper.getIntervalList(getDate("start", "max"), getDate("end", "full"), step);

        List<AccountSum> assets = ChartHelper.sync(
                accountService.getStatsTotal(collectionsHelper.getByName("assets"), step), syncInterval, step
        );

        AccountHelper.addInSeries(assets);
        assets = ChartHelper.sync(assets, interval, step);

        List<AccountSum> liab = ChartHelper.sync(
                accountService.getStatsTotal(collectionsHelper.getByName("liabilities"), step), syncInterval, step
        );
        AccountHelper.addInSeries(liab);
        liab = ChartHelper.sync(liab, interval, step);


        List<Series> series = new ArrayList<Series>();
        series.add(new NormalSeries("Assets", AccountHelper.transformAccountSum(assets), "column"));
        series.add(new NormalSeries("Liabilities", AccountHelper.transformAccountSum(liab, true), "column"));
        series.add(new NormalSeries("Networth", AccountHelper.transformAccountDifference(assets, liab), "line"));

        Graph net = new BarChart();
        net.setSeries(series);
        net.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(net));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        return "report";
    }



    @RequestMapping("/xirr")
    public String xirr(Model model, HttpServletRequest request) {

        String step = getStep(request);
        Date start = parseDate("2010/12");
        Date end = getDate("end", request);

        List<Calendar> interval = ChartHelper.getIntervalList(start, end, step);
        List<Calendar> syncInterval = ChartHelper.getIntervalList(getDate("start", "max"), getDate("end", "full"), step);

        List<Account> in = collectionsHelper.getByName("loanInvestment");
        List<AccountSum> inSum = accountService.getStatsTotalWithoutSiblings(in, step, "Sissekanne");

        List<Account> out = collectionsHelper.getByName("loanInvestment");
        List<AccountSum> outSeries = ChartHelper.sync(accountService.getStatsTotal(out, step), interval, step);


        AccountHelper.addInSeries(outSeries);

        List<Account> red = collectionsHelper.getByName("loanRed");
        List<AccountSum> redSeries = ChartHelper.sync(accountService.getStatsTotal(red, step), interval, step);
        AccountHelper.addInSeries(redSeries);



        List<Series> series = new ArrayList<Series>();
        series.add(new NormalSeries("Optimistic",  AccountHelper.calculateXIRRSeries(inSum, outSeries, interval)));
        series.add(new NormalSeries("Neutral",  AccountHelper.calculateXIRRSeries(inSum, ChartHelper.difference(outSeries, redSeries), interval)));

        Graph line = new LineChart();
        line.setSeries(series);
        line.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        model.addAttribute("chart", new Gson().toJson(line));
        model.addAttribute("period", getPeriod(request));
        model.addAttribute("step", step);

        model.addAttribute("months", interval.size());

        return "report";
    }

    public Date parseDate(String date) {
        try {
            Date parsed = new SimpleDateFormat("y/M").parse(date);
            Calendar cl = Calendar.getInstance();
            cl.setTime(parsed);
            cl.set(Calendar.HOUR, 12);
            return cl.getTime();
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
        cl.setTime(start);
        //cl.set(Calendar.YEAR, 2014);
        //cl.set(Calendar.MONTH, 11);

        switch (period) {
            case "current":
                cl.set(Calendar.MONTH, 0);
                start = cl.getTime();
                break;
            case "previous":
                cl.set(Calendar.YEAR, cl.get(Calendar.YEAR)-1);
                cl.set(Calendar.MONTH, 0);
                start = (Date)cl.getTime().clone();
                cl.set(Calendar.MONTH, 11);
                end = cl.getTime();
                break;
            case "max":
                start = parseDate("2009/01");
                break;
            case "last12":
                end = cl.getTime();
                cl.set(Calendar.MONTH, cl.get(Calendar.MONTH)-12);
                start = cl.getTime();
                break;
            default:
                start = parseDate("2013/01");
        }

        return type.equals("start") ? start : end;
    }

    public String getPeriod(HttpServletRequest request) {
        return request.getParameter("period") == null ? "last12" : request.getParameter("period");
    }

    public String getStep(HttpServletRequest request) {
        return request.getParameter("step") == null ? "month" : request.getParameter("step");
    }

    public int getCumulative(HttpServletRequest request) {
        return request.getParameter("cum") == null ? 0 : 1;
    }

    public void setMenuActive(String key) {
        Menu menu = menuService.getMenu("sidebar");
        menu.setActive(key);
    }
}
