package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.Capability;
import br.edu.ocdrf.racs.model.Entity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(EntityCapability.class)
public class EntityCapability_ { 

    public static volatile SingularAttribute<EntityCapability, Integer> accesslevel;
    public static volatile SingularAttribute<EntityCapability, String> name;
    public static volatile SingularAttribute<EntityCapability, Capability> capabilityFk;
    public static volatile SingularAttribute<EntityCapability, Integer> id;
    public static volatile SingularAttribute<EntityCapability, Integer> type;
    public static volatile SingularAttribute<EntityCapability, Entity> entityFk;

}