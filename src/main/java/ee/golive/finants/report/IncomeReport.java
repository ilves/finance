package ee.golive.finants.report;


import ee.golive.finants.chart.*;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IncomeReport extends Report {

    public IncomeReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "income";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    protected List<Graph> getGraphs() {
        List<Graph> graphs = new ArrayList<Graph>();

        List<List<AccountSum>> incomeAccounts = new ArrayList<>();
        incomeAccounts.add(getSumsA("personalIncome"));
        incomeAccounts.add(getSumsA("companyIncome"));
        incomeAccounts.add(getSumsA("p2pincome"));
        incomeAccounts.add(getSumsA("adv"));
        incomeAccounts.add(getSumsA("realinc"));


        List<String> names1 = new ArrayList<>();
        names1.add("Salary");
        names1.add("Company");
        names1.add("P2P");
        names1.add("Advertisements");
        names1.add("Real Estate");

        Graph graph1 = new StackedBarChart();
        graph1.setSeries(getSeries(incomeAccounts, false, false, names1));
        graph1.setTitle("Income");
        graph1.setYAxisTitle("Income (EUR)");
        graph1.getTooltip().setFormatter("function(){return StackedTotal(this);}");
        graph1.setDateTime();

        List<List<AccountSum>> passiveAccounts = new ArrayList<>();
        passiveAccounts.add(getSumsA("p2pincome"));
        passiveAccounts.add(getSumsA("adv"));
        passiveAccounts.add(getSumsA("realinc"));


        List<String> names2 = new ArrayList<>();
        names2.add("P2P");
        names2.add("Advertisements");
        names2.add("Real Estate");


        Graph graph2 = new StackedBarChart();
        List<Series> graph2series = getSeries(passiveAccounts, false, false, names2);

        graph2series.add(ChartHelper.sumSeriesPoint(graph2series, "Total", "line"));
        graph2.setSeries(graph2series);
        graph2.setTitle("Passive Income");
        graph2.setYAxisTitle("Income (EUR)");
        graph2.getTooltip().setFormatter("function(){return StackedTotal(this);}");
        graph2.setDateTime();

        Graph graph3 = new LineChart();
        List<Series> graph3series = new ArrayList<>();
        graph3series.add(ChartHelper.cumulative(graph2series, "Total", "line"));

        graph3.setSeries(graph3series);
        graph3.setTitle("Passive Income");
        graph3.setYAxisTitle("Income (EUR)");
        graph3.setDateTime();

        graphs.add(graph1);
        graphs.add(graph2);
        graphs.add(graph3);

        return graphs;
    }
}
