package ee.golive.finants.report;


import ee.golive.finants.chart.*;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.AccountHelper;
import ee.golive.finants.helper.ChartHelper;
import ee.golive.finants.model.Account;
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
}
