package ee.golive.finants.chart;

import java.util.List;

public abstract class Series {
    public String name;
    public String type;
    public Long pointStart = null;
    public Long pointInterval = null;

    public abstract List<Float> getData();
}
