package ee.golive.finants.chart;

public class LineChart extends Graph {
    public LineChart() {
        setType("line");
    }

    public void disableDots() {
        Line line = new Line();
        Marker marker = new Marker();
        marker.enabled = false;
        line.marker = marker;
        plotOptions = new PlotOptions();
        plotOptions.line = line;
    }
}
