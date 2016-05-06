package br.edu.ocdrf.entities;

import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.oal.OAOResource;
import br.edu.ocdrf.oal.domain.OResourceEntity;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.mgt.Explain;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class ServiceCore {

    protected String ontologyFileName;
    protected String ontologyRulesFileName;

    protected ServiceID servID = new ServiceID();

    protected RegistrationCertificate regCert;
    protected String serviceCertificateName;
    protected OAOResource oaoResource;
    protected OResourceEntity oResource;
    protected ResourceOCDRF resOntDesc;
    
    protected Model baseModel;
    protected OntModel ontologyDLModel;
    protected InfModel ontoDLWithRulesModel;

    public abstract void initialize() throws ServiceBaseException;

    public void initEntitiesServiceBaseProps() throws ServiceBaseException {
        oResource = new OResourceEntity();
        resOntDesc = new ResourceOCDRF();
    }

    protected void loadOntologyModels(InputStream ontologyFileStream) throws ServiceBaseException {

        try {
            ARQ.setExecutionLogging(Explain.InfoLevel.NONE);

            baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            baseModel.read(ontologyFileStream, "RDF/XML");
            // create the reasoning model using the base
            ontologyDLModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, baseModel);
            // ontoDLWithRulesModel = ModelFactory.createRDFSModel(ontologyDLModel);
        } catch (Exception e) {
            throw new ServiceBaseException(e);
        }
    }

    protected boolean loadCredentialsFile(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException {

        FileInputStream f_in = new FileInputStream(filePath);
        ObjectInputStream obj_in = new ObjectInputStream(f_in);
        servID = (ServiceID) obj_in.readObject();

        System.out.println("Credentials file found:");
        System.out.println(servID.secretKey);
        System.out.println(servID.uuid + "\n");

        f_in.close();
        obj_in.close();

        return servID.areFieldsNullOrEmpty();

    }

    protected void cacheServiceID(String filePath) throws FileNotFoundException, IOException {

        //if (!servID.areFieldsNullOrEmpty()){
        FileOutputStream f_out = new FileOutputStream(filePath);
        ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
        obj_out.writeObject(servID);

        f_out.close();
        obj_out.close();

    }

}
