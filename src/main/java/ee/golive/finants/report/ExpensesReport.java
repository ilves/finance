package ee.golive.finants.report;


import ee.golive.finants.chart.Graph;
import ee.golive.finants.chart.Series;
import ee.golive.finants.chart.StackedBarChart;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ExpensesReport extends Report {

    public ExpensesReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "expenses";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    protected List<Graph> getGraphs() {
        List<Graph> graphs = new ArrayList<>();

        List<List<AccountSum>> incomeAccounts = new ArrayList<>();
        incomeAccounts.add(getSumsA("expenses"));
        incomeAccounts.add(getSumsA("expensesCompany"));

        List<String> names1 = new ArrayList<>();
        names1.add("Personal");
        names1.add("Company");

        List<Series> series = getSeries(incomeAccounts, false, false, names1);
        series.add(ChartHelper.rollingAverage(series.get(0)).setName("Rolling Average").setType("line"));

        Graph graph1 = new StackedBarChart();
        graph1.setSeries(series);
        graph1.setTitle("Expenses");
        graph1.setYAxisTitle("Expenses (EUR)");
        graph1.getTooltip().setFormatter("function(){return StackedTotal(this);}");
        graph1.setDateTime();


        List<Series> seriesCat = getSeries(getSums("expensesCat"), false, false);
        Graph graph2 = new StackedBarChart();
        graph2.setSeries(seriesCat);
        graph2.setTitle("Expenses");
        graph2.setYAxisTitle("Expenses (EUR)");
        graph2.setDateTime();

        graphs.add(graph1);
        graphs.add(graph2);

        return graphs;
    }
}
