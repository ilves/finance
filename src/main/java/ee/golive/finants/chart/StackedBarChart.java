package ee.golive.finants.chart;

public class StackedBarChart extends BarChart {

    public StackedBarChart() {
        setType("column");

        Column column = new Column();
        column.stacking = "normal";
        this.plotOptions = new PlotOptions();
        this.plotOptions.column = column;
    }

    public void setStacking(String stacking) {
        this.plotOptions.column.stacking = stacking;
    }
}
