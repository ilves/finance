package ee.golive.finants.report;


import ee.golive.finants.chart.*;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SavingsReport extends Report {

    public SavingsReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "savings";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    protected List<Graph> getGraphs() {
        List<Graph> graphs = new ArrayList<>();

        List<Account> exp = controller.collectionsHelper.getByName("expenses");
        exp.addAll(controller.collectionsHelper.getByName("expensesCompany"));
        List<Account> inc = controller.collectionsHelper.getByName("personalIncome");
        inc.addAll(controller.collectionsHelper.getByName("companyIncome"));

        List<AccountSum> expSum = ChartHelper.sync(controller.accountService.getStatsTotal(exp, step), interval, step);
        List<AccountSum> incSum = ChartHelper.sync(controller.accountService.getStatsTotal(inc, step), interval, step);

        List<Series> series1 = new ArrayList<Series>();
        series1.add(new PointSeries("Monthly rate", ChartHelper.differencePrecentPoint(incSum, expSum)));
        series1.add(new PointSeries("Long term rate", ChartHelper.differencePrecentSlidingPoint(incSum, expSum)));

        Graph graph1 = new LineChart();
        graph1.setSeries(series1);
        graph1.setDateTime();
        graph1.setTitle("Savings rate");
        graph1.setYAxisTitle("Savings rate [%]");

        Graph graph2 = new LineChart();
        List<AccountSum> personalExp = getSumsA("expenses");
        List<AccountSum> passiveIncome = getSumsA("passiveinc");

        List<List<AccountSum>> list = new ArrayList<>();
        list.add(personalExp);
        list.add(passiveIncome);

        List<Series> series2 = getSeries(list, false, true);


        List<Series> seriesPassive = new ArrayList<Series>();
        seriesPassive.add(ChartHelper.differencePercent(series2.get(1), series2.get(0)).setName("Interval Rate"));
        seriesPassive.add(ChartHelper.differencePercent(
                ChartHelper.cumulativePoint(series2.get(1)),
                ChartHelper.cumulativePoint(series2.get(0))
        ).setName("Sliding Rate"));

        graph2.setSeries(seriesPassive);
        graph2.setDateTime();
        graph2.setTitle("Savings rate");
        graph2.setYAxisTitle("Savings rate [%]");
        graph2.getTooltip().setFormatter("function(){return PercentFormatter(this);}");
        graph2.yAxis.min = 0;


        graphs.add(graph1);
        graphs.add(graph2);

        return graphs;
    }
}
