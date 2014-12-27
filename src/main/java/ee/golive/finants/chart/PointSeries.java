package ee.golive.finants.chart;

import java.util.ArrayList;
import java.util.List;

public class PointSeries extends Series {

    private List<Point> data;

    public PointSeries(String name, List<Point> points, String type) {
        this.data = points;
        this.name = name;
        this.type = type;
    }


    public PointSeries(String name, List<Point> points) {
        this.data = points;
        this.name = name;
    }

    @Override
    public List<Point> getData() {
        return data;
    }
}
