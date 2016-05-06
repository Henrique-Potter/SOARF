package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.EntityCapability;
import br.edu.ocdrf.racs.model.EntityGroup;
import br.edu.ocdrf.racs.model.Group1;
import br.edu.ocdrf.racs.model.Resource;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(Entity.class)
public class Entity_ { 

    public static volatile CollectionAttribute<Entity, EntityGroup> entityGroupCollection;
    public static volatile SingularAttribute<Entity, String> password;
    public static volatile CollectionAttribute<Entity, EntityCapability> entityCapabilityCollection;
    public static volatile SingularAttribute<Entity, String> name;
    public static volatile CollectionAttribute<Entity, Group1> group1Collection;
    public static volatile SingularAttribute<Entity, Integer> id;
    public static volatile SingularAttribute<Entity, String> type;
    public static volatile SingularAttribute<Entity, Integer> securityPolicy;
    public static volatile CollectionAttribute<Entity, Resource> resourceCollection;

}