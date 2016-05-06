package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.OResourceEntity;
import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;

@XStreamAlias("OntologyQuery")
public class OntologyQuery extends XMLSerializedable {

    @XStreamAlias("UseOntologyTriples")
    @XStreamAsAttribute
    private boolean useOntologyTriples;

    @XStreamAlias("SimpleContextQuery")
    @XStreamAsAttribute
    private OntologyContextQuery simpleContextQuery;

    @XStreamImplicit(itemFieldName = "QueryTargets")
    public ArrayList<OntologyQueryTargets> qTargets = new ArrayList<>();

    public OntologyQuery() {

    }

    public OntologyQuery(OntologyQueryTargets target) {
        qTargets.add(target);
    }

    public OntologyQuery(OntologyQueryTriple oTriple) {

        OntologyQueryTargets oTarget = new OntologyQueryTargets(oTriple);

        qTargets.add(oTarget);
    }

    public OntologyContextQuery getSimpleContextQuery() {
        return simpleContextQuery;
    }

    public void setSimpleContextQuery(OntologyContextQuery simpleContextQuery) {
        this.simpleContextQuery = simpleContextQuery;
    }
    
    

    public ArrayList<OntologyQueryTargets> getqTargets() {
        return qTargets;
    }

    public void setqTargets(ArrayList<OntologyQueryTargets> qTargets) {
        this.qTargets = qTargets;
    }

    public void addQueryTarget(OntologyQueryTargets qTarget) {
        qTargets.add(qTarget);
    }

    public boolean isUseOntologyTriples() {
        return useOntologyTriples;
    }

    public void setUseOntologyTriples(boolean customOntologyQuery) {
        this.useOntologyTriples = customOntologyQuery;
    }

    public XStream getOntologyXStreamModel() {
        return ontologyXStreamModel;
    }

    public void setOntologyXStreamModel(XStream ontologyXStreamModel) {
        this.ontologyXStreamModel = ontologyXStreamModel;
    }

    public void searchByUUID(String UUID) {
        OResourceEntity res = new OResourceEntity();
        res.setUUID(UUID);
        OntologyQueryTargets oTarget = new OntologyQueryTargets();
        oTarget.getResEntity().add(res);
        qTargets.add(oTarget);
    }

    public void searchByCapability(String capability) {

        OResourceEntity ores = new OResourceEntity();
        ores.addComponentCapability(capability);

        OntologyQueryTargets oTarget = new OntologyQueryTargets();
        oTarget.getResEntity().add(ores);
        qTargets.add(oTarget);
    }

    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(OntologyQuery.class);
    }

    @Override
    public OntologyQuery createObjectFromXML(String xml) {
        return (OntologyQuery) ontologyXStreamModel.fromXML(xml);
    }

}
