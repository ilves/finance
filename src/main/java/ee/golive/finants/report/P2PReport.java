package ee.golive.finants.report;


import com.google.gson.Gson;
import ee.golive.finants.chart.BarChart;
import ee.golive.finants.chart.Graph;
import ee.golive.finants.chart.LineChart;
import ee.golive.finants.chart.StackedBarChart;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import ee.golive.finants.model.Series;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class P2PReport extends Report {

    public P2PReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "p2p";
    }

    @Override
    protected List<Graph> getGraphs() {
        List<Graph> graphs = new ArrayList<Graph>();

        Graph graph1 = new StackedBarChart();
        graph1.setSeries(getSeries(getSums("p2pincome"), false, false));
        graph1.setTitle("P2P Income");
        graph1.setYAxisTitle("Income (EUR)");
        graph1.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));
        graph1.getTooltip().setFormatter(
            "function(){return StackedTotal(this);}");

        Graph graph2 = new LineChart();
        List<Series> series = getSeries(getSums("p2pdefaulted"), true, false);
        series.add(ChartHelper.sumSeries(series, "Total"));
        graph2.setSeries(series);
        graph2.setTitle("P2P Defaulted loans");
        graph2.setYAxisTitle("Principal (EUR)");
        graph2.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        Graph graph3 = new LineChart();
        List<Series> series3 = getSeries(getSums("p2ploans"), true, false);
        series3.add(ChartHelper.sumSeries(series3, "Total"));
        graph3.setSeries(series3);
        graph3.setTitle("P2P Portfolio size");
        graph3.setYAxisTitle("Principal (EUR)");
        graph3.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        Graph graph4 = new StackedBarChart();
        graph4.setSeries(getSeries(getSums("p2ploans", "hiosa"), false, true));
        graph4.setTitle("Paid principal");
        graph4.setYAxisTitle("Principal (EUR)");
        graph4.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        Graph graph5 = new StackedBarChart();
        graph5.setSeries(getSeries(getSums("p2ploans", "Antud laenud"), false, true));
        graph5.setTitle("New Loans");
        graph5.setYAxisTitle("Principal (EUR)");
        graph5.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));

        List<List<AccountSum>> in = getSums("loanInvestment", "Sissekanne");
        List<List<AccountSum>> out = getSums("loanInvestment");
        List<List<AccountSum>> red = getSums("p2pdefaulted");

        List<AccountSum> inList = getSumsA("loanInvestment", "Sissekanne");
        inList.get(0).setAccount_guid("Total");
        in.add(inList);
        out.add(getSumsA("loanInvestment"));
        red.add(getSumsA("p2pdefaulted"));

        List<Series> xirrSeries = getXIRRSeries(in, out, red);

        Graph graph6 = new LineChart();
        graph6.setSeries(xirrSeries);
        graph6.setCategories(ChartHelper.transformCalendar(interval, new SimpleDateFormat("y/M")));
        graph6.setTitle("XIRR");
        graph6.setYAxisTitle("XIRR (%)");

        graphs.add(graph1);
        graphs.add(graph4);
        graphs.add(graph5);
        graphs.add(graph2);
        graphs.add(graph3);
        graphs.add(graph6);

        return graphs;
    }

    @Override
    public String getTemplate() {
        return "reports";
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
        List<Series> series = new ArrayList<Series>();
        for (List<AccountSum> sum : sums) {
            String n = sum.size()>0?controller.accountService.findByGuid(
                    sum.get(0).getAccount_guid()
            ).getName():"";
            sum = ChartHelper.fillCaps(sum, interval, step);
            if (addInSeries)
                AccountHelper.addInSeries(sum);
            List<AccountSum> synced = ChartHelper.sync(sum, interval, step);
            List<Float> data = AccountHelper.transformAccountSum(synced, abs);
            Series s = new Series(n, data);
            series.add(s);
        }
        return series;
    }

    public List<Series> getXIRRSeries(List<List<AccountSum>> in, List<List<AccountSum>> out, List<List<AccountSum>> red) {
        List<Series> series = new ArrayList<Series>();
        int n = 0;
        System.out.println(in.size());
        System.out.println(out.size());
        System.out.println(red.size());

        for (List<AccountSum> sum : in) {
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
        return new Series(name,
                AccountHelper.calculateXIRRSeries(sum,
                        ChartHelper.difference(out, red), interval));
    }
}
