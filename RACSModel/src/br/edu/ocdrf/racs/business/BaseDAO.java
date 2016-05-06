package br.edu.ocdrf.racs.business;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public abstract class BaseDAO {
    
    protected final EntityManager em;

    public BaseDAO(EntityManagerFactory entityManagerFactory) {
        this.em = entityManagerFactory.createEntityManager();
    }
    

    
}
