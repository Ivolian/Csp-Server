package com.withub.csp.task;

import com.withub.csp.rest.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class HeartbeatTask {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void execute() {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            String sql = "update csp_user set heartbeat = :heartbeat where id = :userId";

            System.out.println(new Date());
            ConcurrentHashMap<String, Date> heartbeatMap = UserController.heartbeatMap;
            for (String userId : heartbeatMap.keySet()) {
                Query query = entityManager.createNativeQuery(sql);
                query.setParameter("heartbeat", heartbeatMap.get(userId));
                query.setParameter("userId", userId);
                query.executeUpdate();
                System.out.println(userId);
            }

            entityTransaction.commit();
            entityManager.close();
            heartbeatMap.clear();

        } catch (Exception e) {
            //
        }

    }

}
