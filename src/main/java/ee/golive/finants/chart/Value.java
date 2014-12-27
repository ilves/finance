package ee.golive.finants.chart;

/**
 * Created by taavi.ilves on 14.10.2014.
 */
public class Value {
    public String id;
    public long value;
    public String color;

    public Value(String id, long value, String color) {
        this.id = id;
        this.value = value;
        this.color = color;
    }

    public Value(String id, long value) {
        this.id = id;
        this.value = value;
    }
}
