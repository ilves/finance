package ee.golive.finants.report;


import com.google.gson.Gson;
import ee.golive.finants.chart.Graph;
import ee.golive.finants.controller.ReportController;
import ee.golive.finants.helper.ChartHelper;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class Report {

    protected ReportController controller;
    protected HttpServletRequest request;
    protected Model model;
    protected List<Calendar> interval;
    protected Date start;
    protected Date end;

    public String menu = "";
    public String step;

    public Report(ReportController controller, HttpServletRequest request, Model model) {
        this.controller = controller;
        this.request = request;
        this.model = model;
        this.step = controller.getStep(request);
        this.start = controller.getDate("start", this.request);
        this.end = controller.getDate("end", this.request);
        this.interval = ChartHelper.getIntervalList(this.start, this.end, this.step);
        run();
    }

    public void run() {
        controller.setMenuActive(getMenu());
        this.step = controller.getStep(request);
        model.addAttribute("charts", getGraphs());
        model.addAttribute("period", controller.getPeriod(request));
        model.addAttribute("step", step);
        model.addAttribute("parser", new Parser());
    }

    protected abstract String getMenu();
    protected abstract List<Graph> getGraphs();
    public abstract String getTemplate();

    public class Parser {
        public String parse(Graph graph) {
            return new Gson().toJson(graph);
        }
    }
}
