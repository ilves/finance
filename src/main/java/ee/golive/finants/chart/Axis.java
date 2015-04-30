package ee.golive.finants.chart;

import java.util.ArrayList;
import java.util.List;

public class Axis {
    public List<String> categories;
    public Text title = new Text();
    public String type;
    public Integer min;
    public Integer max;
    public PlotLine[] plotLines;

    public PlotLine addPlotline() {
        PlotLine line = new PlotLine();
        plotLines = new PlotLine[]{line};
        return line;
    }

    public class PlotLine {
        public Float value;
        public String color = "red";
        public Integer width = 2;
    }
}
