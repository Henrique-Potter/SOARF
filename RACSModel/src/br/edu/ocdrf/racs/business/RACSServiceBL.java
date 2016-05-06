package br.edu.ocdrf.racs.business;

import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Resource;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

public class RACSServiceBL {

    private static final Logger log = Logger.getLogger(RACSServiceBL.class.getName());

    private final EntityManager em;

    public RACSServiceBL(EntityManagerFactory entityManagerFactory) {
        em = entityManagerFactory.createEntityManager();
    }

    public String registerUser(Entity ent) {

        String queryStatus = "";
        try {
            if (isEntityNameRegistered(ent.getName())) {

                em.getTransaction().begin();

                Entity newEntity = ent;

                em.persist(newEntity);
                em.getTransaction().commit();

                queryStatus = "User successfully registered!";

            } else {
                queryStatus = "User Name already registered!";
            }

        } catch (Exception e) {
            log.error(e);
        } finally {
            em.close();
        }
        return queryStatus;
    }

    public void registerUserResource(Entity ent, String uuid, String sercretKey) {
        try {
            em.getTransaction().begin();

            Resource res = new Resource();
            res.setUuid(uuid);
            res.setSecretkey(sercretKey);

            ent.getResourceCollection().add(res);

            em.persist(ent);
            em.getTransaction().commit();

        } catch (Exception e) {
            log.error(e);
        } finally {
            em.close();
        }
    }

    private boolean isEntityNameRegistered(String Name) {

        TypedQuery<Entity> query = em.createNamedQuery("Entity.findByName", Entity.class).setParameter("name", Name);

        List<Entity> results = query.getResultList();

        return !results.isEmpty();
    }

    public Entity getEntityByName(String Name) {

        Entity entity = em.createNamedQuery("Entity.findByName", Entity.class).setParameter("name", Name).getSingleResult();

        return entity;
    }

}
