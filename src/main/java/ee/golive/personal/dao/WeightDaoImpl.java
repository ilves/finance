package ee.golive.personal.dao;


import ee.golive.personal.model.Weight;
import ee.golive.personal.model.Workout;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class WeightDaoImpl {

    @PersistenceContext(unitName = "entityManagerFactory2")
    private EntityManager entityManager;

    @Transactional
    public void save(Weight model) {
        entityManager.persist(model);
    }
    
    @Transactional
    public List<Weight> getAll() {
        return entityManager.createQuery("from Weight order by date_created asc")
                .getResultList();
    }

    @Transactional
    public void saveIfNoExists(Weight model) {
        List oldWeights = entityManager.createQuery("from Weight where DATE(date_created) = DATE(:date)")
                .setParameter("date", model.getDate_created())
                .getResultList();
        if (oldWeights.isEmpty()) save(model);
    }
}