package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Group1;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(EntityGroup.class)
public class EntityGroup_ { 

    public static volatile SingularAttribute<EntityGroup, Group1> groupfk;
    public static volatile SingularAttribute<EntityGroup, Integer> id;
    public static volatile SingularAttribute<EntityGroup, Entity> entityfk;

}