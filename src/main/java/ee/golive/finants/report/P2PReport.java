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

public class P2PReport extends Report {

    public P2PReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "p2p";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    protected List<Graph> getGraphs() {
        List<Graph> graphs = new ArrayList<Graph>();

        Graph graph1 = new StackedBarChart();
        graph1.setSeries(getSeries(getSums("p2pincome"), false, false));
        graph1.setTitle("P2P Income");
        graph1.setYAxisTitle("Income (EUR)");
        graph1.setDateTime();
        graph1.getTooltip().setFormatter("function(){return StackedTotal(this);}");

        Graph graph2 = new LineChart();
        List<Series> series = getSeries(getSums("p2pdefaulted"), true, false);
        series.add(ChartHelper.sumSeriesPoint(series, "Total", "line"));
        graph2.setSeries(series);
        graph2.setDateTime();
        graph2.setTitle("P2P Defaulted loans");
        graph2.setYAxisTitle("Principal (EUR)");

        Graph graph3 = new LineChart();
        List<Series> series3 = getSeries(getSums("p2ploans"), true, false);
        series3.add(ChartHelper.sumSeriesPoint(series3, "Total", "line"));
        graph3.setSeries(series3);
        graph3.setDateTime();
        graph3.setTitle("P2P Portfolio size");
        graph3.setYAxisTitle("Principal (EUR)");

        Graph graph4 = new StackedBarChart();
        graph4.setSeries(getSeries(getSums("p2ploans", "hiosa"), false, true));
        graph4.setTitle("Paid principal");
        graph4.setYAxisTitle("Principal (EUR)");
        graph4.setDateTime();

        Graph graph5 = new StackedBarChart();
        graph5.setSeries(getSeries(getSums("p2ploans", "Antud laenud"), false, true));
        graph5.setTitle("New Loans");
        graph5.setYAxisTitle("Principal (EUR)");
        graph5.setDateTime();

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
        graph6.setTitle("XIRR");
        graph6.setYAxisTitle("XIRR (%)");
        graph6.setDateTime();

        graphs.add(graph1);
        graphs.add(graph4);
        graphs.add(graph5);
        graphs.add(graph2);
        graphs.add(graph3);
        graphs.add(graph6);

        return graphs;
    }
}
