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
import java.util.Arrays;
import java.util.Calendar;
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

        Axis.PlotLine line = graph1.yAxis.addPlotline();
        line.value = 50f;

        Graph graph2 = new LineChart();
        List<AccountSum> personalExp = getSumsA("expenses");
        List<AccountSum> passiveIncome = getSumsA("passiveinc");

        List<List<AccountSum>> list = new ArrayList<>();
        list.add(personalExp);
        list.add(passiveIncome);

        List<Series> series2 = getSeries(list, false, true);


        List<Series> seriesPassive = new ArrayList<Series>();
        seriesPassive.add(ChartHelper.differencePercent(series2.get(1), series2.get(0)).setName("Monthly Rate"));
        seriesPassive.add(ChartHelper.differencePercent(
                ChartHelper.rollingAverage(series2.get(1)),
                ChartHelper.rollingAverage(series2.get(0))
        ).setName("Rolling Average"));

        if (cum == 1)
            seriesPassive.add(getPassiveIncomeGoalSeries());

        graph2.setSeries(seriesPassive);
        graph2.setDateTime();
        graph2.setTitle("Passive Income");
        graph2.setYAxisTitle("Passive Income [% of Expenses]");
        graph2.getTooltip().setFormatter("function(){return PercentFormatter(this);}");
        graph2.yAxis.min = 0;


        Graph graph3 = new LineChart();

        List<List<AccountSum>> netWorthList = new ArrayList<>();
        netWorthList.add(getSumsA("assets"));
        netWorthList.add(getSumsA("liabilities"));

        List<Series> netWorthSeries = getSeries(netWorthList, true, true);
        Series series3_1 = ChartHelper.differencePoint(netWorthSeries.get(0), netWorthSeries.get(1));
        series3_1.setName("Years");
        netWorthSeries.add(series3_1);

        List<List<AccountSum>> expensesAcc = new ArrayList<>();
        expensesAcc.add(getSumsA("expenses"));

        List<Series> series = getSeries(expensesAcc, false, false);
        series.add(ChartHelper.rollingAverage(series.get(0)));

        List<Series> graph3series = new ArrayList<>();
        graph3series.add(ChartHelper.yearsOfExpenses(netWorthSeries.get(2), series.get(1)));


        graph3.setTitle("Total Assets");
        graph3.setYAxisTitle("Years of expenses");
        graph3.setSeries(graph3series);
        graph3.setDateTime();

        graphs.add(graph1);
        graphs.add(graph2);
        graphs.add(graph3);

        return graphs;
    }

    private Series getPassiveIncomeGoalSeries() {
        float total = 15;
        int months = 12;
        float start = 4.74f;
        float step = (total-start)/months;
        float tmp = start;

        List<Point> data = new ArrayList<>();
        Calendar cl = Calendar.getInstance();
        cl.setTime(controller.parseDate("2015/01"));

        System.out.println(cl.getTime());

        for(int n = 0; n < months; n++) {
            tmp+=step;
            data.add(new Point(cl.getTime().getTime(), tmp));
            cl.add(Calendar.MONTH, 1);
        }

        Series series = new PointSeries("GOAL", data);
        return series;
    }
}
