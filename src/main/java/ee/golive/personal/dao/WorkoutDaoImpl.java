package ee.golive.personal.dao;


import ee.golive.personal.model.Weight;
import ee.golive.personal.model.Workout;
import ee.golive.personal.model.WorkoutStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class WorkoutDaoImpl {

    @PersistenceContext(unitName = "entityManagerFactory2")
    private EntityManager entityManager;

    @Transactional
    public void save(Workout model) {
        entityManager.persist(model);
    }
    
    @Transactional
    public void saveIfNotExists(Workout model) {
        List existingModel = entityManager.createQuery("from Workout where source = :source AND source_id = :sid")
                .setParameter("source", model.getSource())
                .setParameter("sid", model.getSource_id())
                .getResultList();
        if (existingModel.isEmpty()) save(model);
    }

    @Transactional
    public List<WorkoutStats> getAllByMonthAndYear() {
        return entityManager.createQuery("select new ee.golive.personal.model.WorkoutStats(" +
                "SUM(duration) AS duration, " +
                "YEAR(date_created) AS year, MONTH(date_created) AS month, date_created)" +
                "from Workout group by YEAR(date_created), MONTH(date_created) order by date_created asc")
                .getResultList();
    }
}