package br.edu.ocdrf.racs.business;

import br.edu.ocdrf.racs.model.Entity;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

public class EntityBL extends BaseDAO {

    private static final Logger log = Logger.getLogger(EntityBL.class.getName());

    public EntityBL(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
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
