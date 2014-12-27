package ee.golive.finants.chart;

import java.util.ArrayList;
import java.util.List;

public abstract class Series {
    public String name;
    public String type;
    public Long pointStart = null;
    public Long pointInterval = null;

    public List<?> getData() {
        return new ArrayList<>();
    }

    public Series setName(String name) {
        this.name = name;
        return this;
    }

    public Series setType(String type) {
        this.type = type;
        return this;
    }
}
