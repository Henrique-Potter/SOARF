package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.OResourceEntity;
import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.LinkedList;

@XStreamAlias("OntologyQueryTargets")
public class OntologyQueryTargets extends XMLSerializedable {

    @XStreamImplicit(itemFieldName = "QueryEntityTarget")
    public ArrayList<OResourceEntity> resEntity = new ArrayList<>();
    
    @XStreamImplicit(itemFieldName = "QueryTriples")
    public LinkedList<OntologyQueryTriple> qTriples = new LinkedList<>();

    public OntologyQueryTargets(){
        
    }

    public ArrayList<OResourceEntity> getResEntity() {
        return resEntity;
    }

    public void setResEntity(ArrayList<OResourceEntity> resEntity) {
        this.resEntity = resEntity;
    }
    
    public OntologyQueryTargets(OntologyQueryTriple queryTriple) {

        qTriples.add(queryTriple);

    }

    public LinkedList<OntologyQueryTriple> getqTriples() {
        return qTriples;
    }

    public void setqTriples(LinkedList<OntologyQueryTriple> qTriples) {
        this.qTriples = qTriples;
    }

    public void addTriple(OntologyQueryTriple queryTriple) {
        qTriples.add(queryTriple);
    }

    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(OntologyQueryTargets.class);
    }

    @Override
    public OntologyQueryTargets createObjectFromXML(String xml) {
        return (OntologyQueryTargets) ontologyXStreamModel.fromXML(xml);
    }

}
