package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.EntityCapability;
import br.edu.ocdrf.racs.model.GroupCapability;
import br.edu.ocdrf.racs.model.Resource;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(Capability.class)
public class Capability_ { 

    public static volatile CollectionAttribute<Capability, EntityCapability> entityCapabilityCollection;
    public static volatile SingularAttribute<Capability, String> name;
    public static volatile SingularAttribute<Capability, Resource> resourceFk;
    public static volatile SingularAttribute<Capability, String> capabilitytype;
    public static volatile SingularAttribute<Capability, Integer> id;
    public static volatile SingularAttribute<Capability, String> capabilityuuid;
    public static volatile CollectionAttribute<Capability, GroupCapability> groupCapabilityCollection;
    public static volatile SingularAttribute<Capability, Integer> accesspolicy;

}