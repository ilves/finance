package ee.golive.finants.chart;

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
    public Legend legend = new Legend();

    public void setMinMax(int min, int max) {
        this.yAxis.min = min;
        this.yAxis.max = max;
    }

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

    public void setDateTime() {
        this.xAxis.type = "datetime";
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public void setZoomChart() {
        this.chart.type = null;
        this.chart.zoomType = "x";
    }

    public class PlotOptions {
        public Column column;
        public Line line;
    }

    public class Column {
        public String stacking;
    }

    public class Line {
        public Marker marker;
    }

    public class Marker {
        public boolean enabled = true;
    }

    public class Tooltip {
        public String pointFormat;
        public Boolean shared;
        public String formatter;

        public void setFormatter(String formatter) {
            this.formatter = formatter;
        }
    }


    public class Legend {
        public Boolean enabled = true;
    }


}
