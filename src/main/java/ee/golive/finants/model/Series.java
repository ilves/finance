package ee.golive.finants.model;

import java.util.List;

public class Series {
    private String name;
    private List<Float> data;

    public Series() {}

    public Series(String name, List<Float> data) {
        this.name = name;
        this.data = data;
    }
}
