package ee.golive.finants.chart;

import java.util.ArrayList;
import java.util.List;

public class ValueSeries extends Series {

    private List<Value> data;
    public String layoutAlgorithm = "squarified";
    public DataLabels dataLabels = new DataLabels();

    public ValueSeries(List<Value> values) {
        this.data = values;
        this.type = "treemap";
    }

    public List<Float> getData() {
        return new ArrayList<Float>();
    }

    public class DataLabels {
        public boolean enabled = true;
        public Style style = new Style();
    }

    public class Style {
        public String color = "white";
        public String fontWeight = "bold";
        public String HcTextStroke = "2px rgba(0,0,0,0.5)";
    }
}
