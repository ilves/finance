package ee.golive.finants.chart;

import com.google.gson.Gson;
import ee.golive.finants.model.Series;

import java.util.List;

public class Chart {
    private List<Series> series;
    private String type;
    private List<String> categories;

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public String getCategoriesJson() {
        return new Gson().toJson(this.categories);
    }

    public String getSeriesJson() {
        return new Gson().toJson(this.series);
    }
}
