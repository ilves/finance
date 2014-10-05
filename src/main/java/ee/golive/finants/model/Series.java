package ee.golive.finants.model;

import java.util.List;

public class Series {
    private String name;
    private List<Float> data;
    private String type;

    public Series() {}

    public Series(String name, List<Float> data) {
        this.name = name;
        this.data = data;
    }

    public Series(String name, List<Float> data, String type) {
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public List<Float> getData() {
        return data;
    }
}
