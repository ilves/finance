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

public class PortfolioReport extends Report {

    public PortfolioReport(ReportController controller, HttpServletRequest request, Model model) {
        super(controller, request, model);
    }

    @Override
    protected String getMenu() {
        return "portfolio";
    }

    @Override
    public String getTemplate() {
        return "reports";
    }

    @Override
    protected List<Graph> getGraphs() {
        List<Graph> graphs = new ArrayList<Graph>();

        List<List<AccountSum>> list = new ArrayList<>();
        list.add(getSumsA("cash"));
        list.add(getSumsA("realEstate"));
        list.add(getSumsA("loan"));
        list.add(getSumsA("shares"));
        list.add(getSumsA("indexfunds"));

        List<String> names = new ArrayList<>();
        names.add("CASH");
        names.add("ESTATE");
        names.add("P2PLOAN");
        names.add("STOCK");
        names.add("INXF");

        List<String> colors = new ArrayList<>();
        colors.add("#7cb5ec");
        colors.add("#434348");
        colors.add("#90ed7d");
        colors.add("#f7a35c");
        colors.add("#8085e9");
        colors.add("#058DC7");
        colors.add("#50B432");
        colors.add("#ED561B");
        colors.add("#DDDF00");
        colors.add("#24CBE5");
        colors.add("#64E572");
        colors.add("#FF9655");
        colors.add("#FFF263");
        colors.add("#6AF9C4");

        List<Series> series1 = getSeries(list, true, true, names);

        StackedBarChart graph1 = new StackedBarChart();
        graph1.setSeries(series1);
        graph1.setTitle("Portfolio");
        graph1.setStacking("percent");
        graph1.setYAxisTitle("");
        graph1.setDateTime();
        graph1.tooltip.pointFormat = "<span style=\"color:{series.color}\">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>";
        graph1.tooltip.shared = true;

        List<Value> vals = new ArrayList<>();
        int n = 0;
        for(Series s : series1) {
            PointSeries tmp = (PointSeries) s;
            vals.add(new Value(s.name, (long) tmp.getData().get(s.getData().size()-1).y.floatValue(), colors.get(n)));
            n++;
        }

        ValueSeries testSeries = new ValueSeries(vals);
        List<Series> series = new ArrayList<>();
        series.add(testSeries);
        TreemapChart graph2 = new TreemapChart();
        graph2.setSeries(series);
        graph2.setTitle("Assets Treemap");



        List<List<AccountSum>> list2 = new ArrayList<>();
        list2.addAll(getSums("shares"));
        list2.addAll(getSums("indexfunds"));
        list2.addAll(getSums("realEstate"));
        list2.add(getSumsA("bondora"));
        list2.add(getSumsA("moneyzen"));
        list2.add(getSumsA("omaraha"));

        List<String> names2 = new ArrayList<>();
        names2.add("OEG1T");
        names2.add("TVEAT");
        names2.add("TKM1T");
        names2.add("SPY");
        names2.add("Crowdestate");
        names2.add("EstateGuru");
        names2.add("Bondora");
        names2.add("MoneyZen");
        names2.add("Omaraha");


        List<Series> series2 = getSeries(list2, true, true, names2);

        List<Value> vals2 = new ArrayList<>();
        n = 0;
        for(Series s : series2) {
            PointSeries tmp = (PointSeries) s;
            vals2.add(new Value(s.name, (long) tmp.getData().get(s.getData().size()-1).y.floatValue(), colors.get(n)));
            n++;
        }


        ValueSeries testSeries2 = new ValueSeries(vals2);

        List<Series> series3 = new ArrayList<>();
        series3.add(testSeries2);

        TreemapChart graph3 = new TreemapChart();
        graph3.setSeries(series3);
        graph3.setTitle("Investments Treemap");

        graphs.add(graph1);
        graphs.add(graph2);
        graphs.add(graph3);

        return graphs;
    }
}
