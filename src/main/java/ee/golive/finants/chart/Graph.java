package ee.golive.finants.chart;

import ee.golive.finants.model.Series;

import java.util.List;

public class Graph {

    public Chart chart = new Chart();
    public List<Series> series;
    public PlotOptions plotOptions;
    public Axis xAxis = new Axis();
    public Tooltip tooltip = new Tooltip();

    public void setType(String type) {
        this.chart.type = type;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public void setCategories(List<String> categories) {
        this.xAxis.categories = categories;
    }

    public class PlotOptions {
        public Column column;
    }

    public class Column {
        public String stacking;
    }

    public class Tooltip {
        public String pointFormat;
        public Boolean shared;
    }
}
