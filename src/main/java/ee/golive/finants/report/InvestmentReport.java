package ee.golive.finants.report;


import ee.golive.finants.chart.*;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.helper.FinanceHelper;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class InvestmentReport extends Report {

    public InvestmentReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "investing";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    public void setGraphs() {
        setInvestmentsGraph();
        setInvestmentsPerformanceGraph();
        setInvestmentsROIGraph();
    }

    private void setInvestmentsGraph() {
        Graph investmentsGraph = new StackedBarChart();
        investmentsGraph.setDateTime();
        List<List<AccountSum>> list = new ArrayList<>();
        list.add(getSumsA("realEstate", "Sissekanne|Ostmine|MUUK"));
        list.add(getSumsA("loanInvestment", "Sissekanne|Ostmine|MUUK"));
        list.add(getSumsA("shares", "Sissekanne|Ostmine|MUUK"));
        list.add(getSumsA("indexfunds", "Sissekanne|Ostmine|MUUK"));
        List<String> names = new ArrayList<>();
        names.add("ESTATE");
        names.add("P2P");
        names.add("STOCK");
        names.add("INXF");

        List<Series> investments = getSeries(list, (cum==1), (cum==1), true, names);
        if (cum == 1) {
            Series tmp = ChartHelper.cumulativePoint(investments, "Total", "column");
            investments = new ArrayList<>();
            investments.add(tmp);
            investments.add(getInvestmentGoalSeries().setType("line"));
        }

        investmentsGraph.setSeries(investments);
        investmentsGraph.setTitle("Performance of the investments");
        investmentsGraph.setYAxisTitle("Value (EUR)");
        investmentsGraph.getTooltip().setFormatter("function(){return StackedTotal(this);}");

        graphs.add(investmentsGraph);
    }

    private void setInvestmentsPerformanceGraph() {
        Graph portfolioValue = new BarChart();
        portfolioValue.setDateTime();
        List<List<AccountSum>> list2 = new ArrayList<>();

        list2.add(getSumsA("investments", "Sissekanne|Ostmine|MUUK"));
        list2.add(getSumsA("investments"));
        List<Series> portfolioValuesTmp = getSeries(list2, true, true, Arrays.asList("Investments", "Real value"));
        List<Series> portfolioValues = new ArrayList<>();


        portfolioValues.add(ChartHelper.toAreaRange(portfolioValuesTmp.get(0), portfolioValuesTmp.get(1)));
        portfolioValue.setSeries(portfolioValues);
        portfolioValue.setTitle("Performance of the investments");
        portfolioValue.setYAxisTitle("Value (EUR)");
        portfolioValue.setType("arearange");
        graphs.add(portfolioValue);
    }

    private void setInvestmentsROIGraph() {
        Graph roiGraph = new LineChart();
        roiGraph.setDateTime();
        List<List<AccountSum>> list2 = new ArrayList<>();

        // TOTAL
        list2.add(getSumsA("investments", "Sissekanne|Ostmine|MUUK"));
        list2.add(getSumsA("investments"));

        // P2P
        list2.add(getSumsA("loanInvestment", "Sissekanne"));
        list2.add(getSumsA("loanInvestment"));


        List<Series> roiValuesTotalTmp = getSeries(list2.subList(0,2), true, true, Arrays.asList("Total ROI", ""));
        List<Series> roiValuesP2PTmp = getSeries(list2.subList(2,4), true, true, Arrays.asList("P2P ROI", ""));


        List<Series> roiValues = new ArrayList<>();


        roiValues.add(ChartHelper.differencePercent(roiValuesTotalTmp.get(0), roiValuesTotalTmp.get(1), true));
        roiValues.add(ChartHelper.differencePercent(roiValuesP2PTmp.get(0), roiValuesP2PTmp.get(1), true));

        roiGraph.setSeries(roiValues);
        roiGraph.setTitle("Performance of the investments");
        roiGraph.setYAxisTitle("Value (EUR)");
        graphs.add(roiGraph);

    }


    private Series getInvestmentGoalSeries() {
        float total = 16600f;
        int months = 12;
        float step = total/months;
        float tmp = 0;

        List<Point> data = new ArrayList<>();
        Calendar cl = Calendar.getInstance();
        cl.setTime(controller.parseDate("2015/01/01"));

        for(int n = 0; n < months; n++) {
            tmp+=step;
            data.add(new Point(cl.getTime().getTime(), tmp));
            cl.add(Calendar.MONTH, 1);
        }

        Series series = new PointSeries("STEPS", data);
        return series;
    }
}
