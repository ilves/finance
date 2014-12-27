package ee.golive.finants.chart;

import java.util.List;

/**
 * Created by taavi.ilves on 14.10.2014.
 */
public class NormalSeries extends Series {
    private List<Float> data;

    public NormalSeries(List<Float> data) {
        this.data = data;
    }

    public NormalSeries(String name, List<Float> data) {
        this.name = name;
        this.data = data;
    }

    public NormalSeries(String name, List<Float> data, String type) {
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public NormalSeries setType(String type) {
        this.type = type;
        return this;
    }

    public List<Float> getData() {
        return data;
    }
}
