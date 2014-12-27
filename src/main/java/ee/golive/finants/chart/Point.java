package ee.golive.finants.chart;

/**
 * Created by taavi.ilves on 14.10.2014.
 */
public class Point {
    public Float y;
    public Long x;
    public Float low;
    public Float high;

    public Point(Long x, Float y) {
        this.y = y;
        this.x = x;
    }

    public Point(Long x, Float min, Float max) {
        this.x = x;
        this.high = max;
        this.low = min;
    }
}
