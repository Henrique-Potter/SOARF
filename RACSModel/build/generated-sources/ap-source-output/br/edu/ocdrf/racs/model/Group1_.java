package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.EntityGroup;
import br.edu.ocdrf.racs.model.GroupCapability;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(Group1.class)
public class Group1_ { 

    public static volatile CollectionAttribute<Group1, EntityGroup> entityGroupCollection;
    public static volatile SingularAttribute<Group1, Entity> creatingEntity;
    public static volatile SingularAttribute<Group1, String> name;
    public static volatile SingularAttribute<Group1, Integer> id;
    public static volatile CollectionAttribute<Group1, GroupCapability> groupCapabilityCollection;

}