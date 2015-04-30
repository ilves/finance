package ee.golive.personal.controller;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.error.LoginException;
import com.moomeen.endo2java.model.Workout;
import ee.golive.finants.chart.*;
import ee.golive.finants.report.Report;
import ee.golive.personal.dao.WeightDaoImpl;
import ee.golive.personal.dao.WorkoutDaoImpl;
import ee.golive.personal.model.Weight;
import ee.golive.personal.model.WorkoutStats;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class PersonalHomeController {

    @Autowired
    WorkoutDaoImpl workoutDao;

    @Autowired
    WeightDaoImpl weightDao;

    @RequestMapping("/endomondo-import")
    public String endomondo(Model model) {

        EndomondoSession session = new EndomondoSession("ilves.taavi@gmail.com", "");
        try {
            session.login();
            List<Workout> workoutList = session.getWorkouts();
            for(Workout workout : workoutList) {
                ee.golive.personal.model.Workout newWorkout = new ee.golive.personal.model.Workout();
                newWorkout.setSource_id(workout.getId().toString());
                newWorkout.setSource("ENDOMONDO");
                newWorkout.setSport_type(workout.getSport().name());
                newWorkout.setCalories(workout.getCalories());
                newWorkout.setDate_created(workout.getStartTime().toDate());
                newWorkout.setDuration(workout.getDuration().getStandardSeconds());
                workoutDao.saveIfNotExists(newWorkout);
            }
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InvocationException e) {
            e.printStackTrace();
        }

        return "dashboard";
    }

    @RequestMapping("/endomondo")
    public String endomondoStats(Model model) {
        List<WorkoutStats> statsByMonth = workoutDao.getAllByMonthAndYear();

        List<Series> series = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        Series weightSeries = new PointSeries("Duration", points, "line");
        series.add(weightSeries);

        for(WorkoutStats workoutStat : statsByMonth) {
            Seconds s1 = Seconds.seconds((int)workoutStat.getDuration());
            Calendar cl = Calendar.getInstance();
            cl.setTime(workoutStat.getDate_created());
            cl.set(Calendar.DATE, 1);
            cl.set(Calendar.HOUR, 12);
            cl.set(Calendar.MINUTE, 0);
            cl.set(Calendar.SECOND, 0);
            Point tmp = new Point(cl.getTime().getTime(), (float)s1.toStandardHours().getHours());
            points.add(tmp);
        }

        Graph graph1 = new Graph();
        graph1.setSeries(series);
        graph1.setTitle("Duration");
        graph1.setYAxisTitle("Duration (H)");
        graph1.setDateTime();
        graph1.setZoomChart();

        model.addAttribute("chart", graph1);
        model.addAttribute("parser", new Report.Parser());


        return "personal/endomondo";
    }

    @RequestMapping("/weight")
    public String weight(Model model) {
        List<Weight> weights = weightDao.getAll();

        model.addAttribute("weights", weights);
        model.addAttribute("weight", new Weight());

        List<Series> series = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        for(Weight weight : weights) {
            Point tmp = new Point(weight.getDate_created().getTime(), weight.getWeight().floatValue());
            points.add(tmp);
        }

        Series weightSeries = new PointSeries("Weight", points);
        series.add(weightSeries);

        Graph graph1 = new LineChart();
        graph1.setSeries(series);
        graph1.setTitle("Weight");
        graph1.setYAxisTitle("Weight (KG)");
        graph1.setDateTime();

        model.addAttribute("chart", graph1);
        model.addAttribute("parser", new Report.Parser());


        return "personal/weight";
    }

    @RequestMapping(value = "/weight/add", method = RequestMethod.POST)
    public String addStudent(@ModelAttribute Weight weight, Model model, BindingResult errors) {
        weightDao.saveIfNoExists(weight);
        return weight(model);
    }
}
