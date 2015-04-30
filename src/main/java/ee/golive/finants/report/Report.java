package ee.golive.finants.report;


import com.google.gson.Gson;
import ee.golive.finants.chart.*;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class Report {

    protected ReportController controller;
    protected HttpServletRequest request;
    protected Model model;
    protected List<Calendar> interval;
    protected Date start;
    protected Date end;
    protected List<Graph> graphs = new ArrayList<>();


    public String menu = "";
    public String step;
    public int cum;

    public Report(ReportController controller, HttpServletRequest request, Model model) {
        this.controller = controller;
        this.request = request;
        this.model = model;
        this.step = controller.getStep(request);
        this.start = controller.getDate("start", this.request);
        this.end = controller.getDate("end", this.request);
        this.interval = ChartHelper.getIntervalList(this.start, this.end, this.step);
        this.cum = controller.getCumulative(request);
        run();
    }

    public void run() {
        controller.setMenuActive(getMenu());
        this.step = controller.getStep(request);
        model.addAttribute("charts", getGraphs());
        model.addAttribute("period", controller.getPeriod(request));
        model.addAttribute("step", step);
        model.addAttribute("parser", new Parser());
        model.addAttribute("cum", cum);

        Calendar end = Calendar.getInstance();
        end.setTime(this.end);
        Calendar start = Calendar.getInstance();
        start.setTime(this.start);

        model.addAttribute("end", end);
        model.addAttribute("start", start);

    }

    protected abstract String getMenu();

    protected List<Graph> getGraphs() {
        setGraphs();
        return this.graphs;
    }

    public void setGraphs() {

    }

    public abstract String getTemplate();

    public static class Parser {
        public String parse(Graph graph) {
            return parseFunctions(new Gson().toJson(graph));
        }

        public String parseFunctions(String json) {
            json = json.replace("\"function(){","function(){");
            json = json.replace("}\"","}");
            return json;
        }
    }


    public List<List<AccountSum>> getSums(String name) {
        List<Account> accounts = controller.collectionsHelper.getByName(name);
        List<List<AccountSum>> sums = controller.accountService.getStats(accounts, step);
        return sums;
    }

    public List<List<AccountSum>> getSums(String name, String description) {
        List<Account> accounts = controller.collectionsHelper.getByName(name);
        List<List<AccountSum>> sums = controller.accountService.getStats(accounts, step, description);
        return sums;
    }

    public List<AccountSum> getSumsA(String name, String description) {
        List<Account> accounts = controller.collectionsHelper.getByName(name);
        List<AccountSum> sums = controller.accountService.getStatsTotalWithoutSiblings(accounts, step, description);
        return sums;
    }

    public List<AccountSum> getSumsA(String name) {
        List<Account> accounts = controller.collectionsHelper.getByName(name);
        List<AccountSum> sums = controller.accountService.getStatsTotal(accounts, step);
        return sums;
    }

    public List<Series> getSeries(List<List<AccountSum>> sums, Boolean addInSeries, Boolean abs) {
        return getSeries(sums, addInSeries, false, abs, null);
    }

    public List<Series> getSeries(List<List<AccountSum>> sums, Boolean addInSeries, Boolean abs, List<String> names) {
        return getSeries(sums, addInSeries, false, abs, names);
    }

    public List<Series> getSeries(List<List<AccountSum>> sums, Boolean addInSeries, Boolean after, Boolean abs, List<String> names) {
        List<Series> series = new ArrayList<>();
        int m = 0;
        for (List<AccountSum> sum : sums) {
            Account acc = sum.size() > 0 ? controller.accountService.findByGuid(sum.get(0).getAccount_guid()) : null;
            String n = names != null ?  names.get(m) :
                    (acc != null ? acc.getName() :
                        sum.size() > 0 ? sum.get(sum.size()-1).getName() : "");
            sum = ChartHelper.fillCaps(sum, interval, step);
            if (addInSeries && !after)
                AccountHelper.addInSeries(sum);
            List<AccountSum> synced = ChartHelper.sync(sum, interval, step);
            if (addInSeries && after)
                AccountHelper.addInSeries(sum);
            List<Point> data = AccountHelper.transformAccountSum(synced, abs, (Calendar)interval.get(0).clone(), getTypeCal());
            Series s = new PointSeries(n, data);
            series.add(s);
            m++;
        }

        return series;
    }

    private int getTypeCal() {
        return step.equals("year") ? Calendar.YEAR : Calendar.MONTH;
    }

    public List<Series> getXIRRSeries(List<List<AccountSum>> in, List<List<AccountSum>> out, List<List<AccountSum>> red) {
        List<Series> series = new ArrayList<Series>();
        int n = 0;

        for (List<AccountSum> sum : in) {
            if (out.size() > n && red.size() > n)
                series.add(getXIRRSerie(sum, out.get(n), red.get(n)));
            n++;
        }
        return series;
    }

    public Series getXIRRSerie(List<AccountSum> sum, List<AccountSum> out, List<AccountSum> red) {
        red = ChartHelper.fillCaps(red, interval, step);

        AccountHelper.addInSeries(out);
        AccountHelper.addInSeries(red);

        Account acc = controller.accountService.findByGuid(sum.get(0).getAccount_guid());
        String name = acc != null ? acc.getName() : sum.get(0).getAccount_guid();
        return new NormalSeries(name,
                AccountHelper.calculateXIRRSeries(sum,
                        ChartHelper.difference(out, red), interval));
    }
}
