package ee.golive.finants.report;


import ee.golive.finants.chart.BarChart;
import ee.golive.finants.chart.Graph;
import ee.golive.finants.chart.Series;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.AccountSum;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworthReport extends Report {

    public NetworthReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "networth";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    public void setGraphs() {
        Graph netWorth = new BarChart();
        netWorth.setDateTime();

        List<List<AccountSum>> list = new ArrayList<>();
        list.add(getSumsA("assets"));
        list.add(getSumsA("liabilities"));

        List<Series> netWorthSeries = getSeries(list, true, true, Arrays.asList("Assets", "Liabilities"));
        netWorthSeries.get(0).type = "column";
        netWorthSeries.get(1).type = "column";

        netWorthSeries.add(
                ChartHelper.differencePoint(netWorthSeries.get(0), netWorthSeries.get(1))
                        .setName("NetWorth").setType("line")
        );

        netWorth.setSeries(netWorthSeries);
        netWorth.setTitle("Net Worth (Wealth)");
        netWorth.setYAxisTitle("Wealth (EUR)");

        graphs.add(netWorth);
    }
}
