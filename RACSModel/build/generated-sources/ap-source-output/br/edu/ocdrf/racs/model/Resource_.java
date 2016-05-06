package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.Capability;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Resource;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(Resource.class)
public class Resource_ { 

    public static volatile SingularAttribute<Resource, String> secretkey;
    public static volatile SingularAttribute<Resource, String> serviceUrl;
    public static volatile SingularAttribute<Resource, Resource> directoryServiceFk;
    public static volatile SingularAttribute<Resource, Integer> id;
    public static volatile SingularAttribute<Resource, Integer> securitypolicy;
    public static volatile SingularAttribute<Resource, Integer> type;
    public static volatile SingularAttribute<Resource, Entity> entityfk;
    public static volatile CollectionAttribute<Resource, Resource> resourceCollection;
    public static volatile SingularAttribute<Resource, String> uuid;
    public static volatile CollectionAttribute<Resource, Capability> capabilityCollection;

}