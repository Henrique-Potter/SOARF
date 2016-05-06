package br.edu.ocdrf.racs.business;

import br.edu.ocdrf.oal.domain.OCapability;
import br.edu.ocdrf.oal.domain.OResourceComponent;
import br.edu.ocdrf.racs.model.Capability;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.EntityCapability;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

public class CapabilityBL extends BaseDAO {

    private final int ACCESS_LEVEL_NAME_ONLY = 0;
    private final int ACCESS_LEVEL_READ_ONLY = 1;
    private final int ACCESS_LEVEL_EXECUTION = 2;

    private final int ACCESS_TYPE_BLOCKED = 0;
    private final int ACCESS_TYPE_ALLOWED = 1;
    private final int ACCESS_TYPE_REQUEST = 2;

    private final int ACCESS_POLICY_RESTRICTIVE = 0;
    private final int ACCESS_POLICY_LIBERAL = 1;
    private final int ACCESS_POLICY_ONDEMAND = 2;

    private static final Logger log = Logger.getLogger(RACSServiceBL.class.getName());

    public CapabilityBL(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public List<OResourceComponent> setOnlyAllowedResCompAccesses(List<OResourceComponent> resCompList, Entity entity) {

        Iterator<OResourceComponent> iter = resCompList.iterator();

        while (iter.hasNext()) {
            OResourceComponent res = iter.next();

            checkCapabilityList(res.getCapabilities(), entity);

            removerUnauthorizedCapabilities(res.getCapabilities());

            if (res.getCapabilities().isEmpty()) {
                iter.remove();
            }
        }

        return resCompList;

    }

    public List<OCapability> setOnlyAllowedCapabilityAccesses(List<OCapability> capList, Entity entity) {

        checkCapabilityList(capList, entity);
        removerUnauthorizedCapabilities(capList);

        return capList;

    }

    private void checkCapabilityList(List<OCapability> capList, Entity entity) {

        for (OCapability oCap : capList) {

            if (oCap.accessPolicy == ACCESS_POLICY_RESTRICTIVE) {
                oCap.accessControlCheck = false;
            }
            
            if (oCap.accessPolicy == ACCESS_POLICY_LIBERAL || oCap.accessPolicy == ACCESS_POLICY_ONDEMAND) {
                oCap.accessControlCheck = true;
            }

            for (EntityCapability entCap : entity.getEntityCapabilityCollection()) {

                Capability cap = entCap.getCapabilityFk();

                if (oCap.getNodeId().equals(cap.getName())) {
                    if (entCap.getType() == ACCESS_TYPE_ALLOWED) {
                        oCap.accessControlCheck = true;
                    }
                    if (entCap.getType() == ACCESS_TYPE_BLOCKED) {
                        oCap.accessControlCheck = false;
                    }
                }
                
            }
        }
    }

    protected void removerUnauthorizedCapabilities(List<OCapability> list) {
        Iterator<OCapability> iter = list.iterator();
        while (iter.hasNext()) {
            OCapability cap = iter.next();
            if (cap.accessControlCheck == false) {
                iter.remove();
            }
        }
    }

}
