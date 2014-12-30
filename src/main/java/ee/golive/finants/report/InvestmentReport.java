package ee.golive.finants.report;


import ee.golive.finants.chart.*;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
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
        List<Series> investments = getSeries(list, false, true, names);
        investmentsGraph.setSeries(investments);
        investmentsGraph.setTitle("Performance of the investments");
        investmentsGraph.setYAxisTitle("Value (EUR)");
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
        list2.add(getSumsA("investments", "Sissekanne|Ostmine|MUUK"));
        list2.add(getSumsA("investments"));
        List<Series> roiValuesTmp = getSeries(list2, true, true, Arrays.asList("Investments", "Real value"));
        List<Series> roiValues = new ArrayList<>();
        roiValues.add(ChartHelper.differencePercent(roiValuesTmp.get(0), roiValuesTmp.get(1), true));
        roiGraph.setSeries(roiValues);
        roiGraph.setTitle("Performance of the investments");
        roiGraph.setYAxisTitle("Value (EUR)");
        graphs.add(roiGraph);

    }


}
