package ee.golive.finants.chart;

import ee.golive.finants.model.Series;

import javax.tools.Tool;
import java.util.List;

public class Graph {

    public Chart chart = new Chart();
    public List<Series> series;
    public PlotOptions plotOptions;
    public Axis xAxis = new Axis();
    public Axis yAxis = new Axis();
    public Tooltip tooltip = new Tooltip();
    public Text title = new Text();
    public Credits credits = new Credits();

    public void setType(String type) {
        this.chart.type = type;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public void setCategories(List<String> categories) {
        this.xAxis.categories = categories;
    }

    public void setTitle(String title) {
        this.title.text = title;
    }

    public Text getTitle() { return this.title; }

    public void setYAxisTitle(String title) {
        this.yAxis.title.text = title;
    }

    public Tooltip getTooltip() {
        return tooltip;
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
        public String formatter;

        public void setFormatter(String formatter) {
            this.formatter = formatter;
        }
    }


}
