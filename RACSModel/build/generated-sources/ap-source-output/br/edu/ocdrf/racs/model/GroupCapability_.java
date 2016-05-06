package br.edu.ocdrf.racs.model;

import br.edu.ocdrf.racs.model.Capability;
import br.edu.ocdrf.racs.model.Group1;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-04-12T17:05:04")
@StaticMetamodel(GroupCapability.class)
public class GroupCapability_ { 

    public static volatile SingularAttribute<GroupCapability, Integer> accesslevel;
    public static volatile SingularAttribute<GroupCapability, String> name;
    public static volatile SingularAttribute<GroupCapability, Capability> capabilityFk;
    public static volatile SingularAttribute<GroupCapability, Group1> groupFk;
    public static volatile SingularAttribute<GroupCapability, Integer> id;
    public static volatile SingularAttribute<GroupCapability, Integer> type;

}