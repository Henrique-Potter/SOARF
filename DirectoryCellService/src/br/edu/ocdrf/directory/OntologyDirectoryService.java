package br.edu.ocdrf.directory;

import br.edu.ocdrf.entities.ServiceID;
import br.edu.ocdrf.entities.ServicesTypeID;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.message.ResourceOntologyRegistrationData;
import br.edu.ocdrf.racs.business.ResourceBL;
import br.edu.ocdrf.racs.model.Capability;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.util.UniquefyOntologyNodes;
import br.edu.ocdrf.ws.wsdl.util.UUIDServiceWSUtil;
import br.edu.ocdrf.util.xml.XMLUtility;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.jena.riot.RDFDataMgr;

import org.apache.log4j.Logger;

public abstract class OntologyDirectoryService extends AbstractDirectoryService {

    private static final Logger log = Logger.getLogger(UUIDServiceWSUtil.class.getName());

    @Override
    public void initialize() throws DirectoryServiceException {

        try {

            initServiceBaseProps();
            File configFile = new File(getMainServicesConfigPath(), CONFIG_FILE_NAME);

            if (configFile.exists()) {
                log.info("Loading config file...");
                readConfig(configFile);
                manageCachedID();
                log.info("Config file successfully loaded!\n");
                log.info("Loading ontology file...");
                loadInitialOntology(resOntDesc.getOntologyFileName(), resOntDesc.getOntologyRulesFileName());

            } else {
                log.error("Directory Service configuration file not found: " + configFile.toString());
                System.exit(4);
            }

        } catch (ServiceBaseException serviceBaseException) {
            throw new DirectoryServiceException(serviceBaseException);
        }

    }

    private void manageCachedID() throws DirectoryServiceException {

        String fullFilePath = null;
        try {
            fullFilePath = getMainServicesConfigPath()+"ontoData/" + ID_FILE_NAME;
            loadCredentialsFile(fullFilePath);
        } catch (FileNotFoundException fileEX) {
            try {
                servID = registerResource(servID.loginID, servID.password);
                cacheServiceID(fullFilePath);

            } catch (Exception iOException) {
                throw new DirectoryServiceException(iOException);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new DirectoryServiceException(e);
        }
    }

    protected ServiceID registerResource(String userId, String password) throws Exception {

        ResourceBL resbl = new ResourceBL(emf);
        return resbl.registerDirService(userId, password, resOntDesc.getInvokeTecnology(WEBSERVICE_INV_TECH));

    }

    private void loadInitialOntology(String ontFileName, String ontRules) throws DirectoryServiceException {
        loadOntologyFile(ontFileName, ontRules);
        //testDirectorySrvOntology();
    }

    protected void loadOntologyFile(String ontologyFileName, String rulesFileName) throws DirectoryServiceException {
        try {

            loadOntologyModelsWithRules(ontologyFileName, rulesFileName);
            log.info("Base Ontology succesfully loaded!\n");

            //Setting directory service uui
            //setResourceUUID(ontoDLWithRulesModel, JenaUUID.generate().asString());
            //ontoDLWithRulesModel.prepare();
            //oaoResource = new OAOResource(ontoDLWithRulesModel);

        } catch (ServiceBaseException ex) {
            throw new DirectoryServiceException(ex);
        }

    }

    public synchronized ServiceID addResourceToModel(String ontologyRDFXML, Entity user, ResourceOntologyRegistrationData data) throws DirectoryServiceException {


        ServiceID resourceServiceID;
        String uuid;

        try {

            String uniquefiedOntology = UniquefyOntologyNodes.uniquefyOntology(ontologyRDFXML);
            uuid = UniquefyOntologyNodes.resUUID;

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(uniquefiedOntology.getBytes())) {
                OntModel tempBase = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
                tempBase.read(inputStream, "RDF/XML");

                dataset.begin(ReadWrite.WRITE);
                Model model = dataset.getDefaultModel();
                model.add(tempBase);
                dataset.commit();
                dataset.end();
                tempBase.close();
                model.close();
            }

            ResourceBL resbl = new ResourceBL(emf);
            resourceServiceID = resbl.registerService(user, servID.uuid, uuid, ServicesTypeID.RESOURCE_AGENT_SERVICE_TYPE, data.serviceUrl);

            registerResourceCapabilities(uuid, uniquefiedOntology);

            return resourceServiceID;
            //testDirectorySrvOntology();
            //log.info("Resource successfully registered!");
        } catch (Exception e) {
            System.err.println("Error while registering new resource.\n\n");
            throw new DirectoryServiceException(e);
        }

    }

    @Override
    protected void readConfig(File file) throws ServiceBaseException {

        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            reader = factory.createXMLStreamReader(inputStream);

            while (true) {
                int event = reader.next();
                if (event == XMLStreamConstants.END_DOCUMENT) {
                    reader.close();
                    break;
                }

                if (event == XMLStreamConstants.START_ELEMENT) {

                    switch (reader.getLocalName()) {
                        case "DirectoryService":
                            //TODO extra service config
                            break;
                        case "Login":
                            servID.loginID = XMLUtility.getAttributeValue(reader, "UserID");
                            servID.password = XMLUtility.getAttributeValue(reader, "Password");
                            //TODO extra service config
                            break;
                        case "Ontology":
                            resOntDesc.setOntologyFileName(XMLUtility.getAttributeValue(reader, "File"));
                            resOntDesc.setOntologyRulesFileName(XMLUtility.getAttributeValue(reader, "Rules"));

                            break;
                        case "Tecnology":
                            resOntDesc.setInvokeTecnology(XMLUtility.getAttributeValue(reader, "type"), XMLUtility.getAttributeValue(reader, "url"));
                            break;
                    }
                }
            }
        } catch (XMLStreamException | IOException e) {
            throw new DirectoryServiceException(e);
        } finally {
            closeStreams(reader, inputStream);
        }
    }

    @Override
    protected void startNRDManager(long period) {

        nrdrTimer = new Timer();
        nrdrTimer.scheduleAtFixedRate(new DeviceStatusObserver(dataset), new Date().getTime() + period, period);
    }

    private void testDirectorySrvOntology() {

        log.info("Preparing to test directory service loaded ontology...");

        com.hp.hpl.jena.rdf.model.Resource directoryResource = ontologyDLModel.getResource(oaoResource.getOWL_NSPREFIX() + "DirectoryService01");
        com.hp.hpl.jena.rdf.model.Resource directoryCapacity = ontologyDLModel.getResource(oaoResource.getOWL_NSPREFIX() + "DirectoryService");

        StmtIterator si = ontoDLWithRulesModel.listStatements(directoryResource, ontologyDLModel.getProperty(oaoResource.getOWL_NSPREFIX() + "hasCapacity"), directoryCapacity);
        if (si.hasNext()) {
            log.info("Base Ontology test successfull! \n");
        } else {
            log.info("Directory instance not found in loaded ontology! \n");
        }

    }

    private String createLocalUUID() {

        String uuid = UUID.randomUUID().toString();
        log.info("UUID successfully generated!");

        return uuid;

    }

    public void registerResourceCapabilities(String uuid, String ontologyXML) throws DirectoryServiceException, ServiceBaseException {

        ResourceBL dtAccess = new ResourceBL(emf);

        br.edu.ocdrf.racs.model.Resource res = dtAccess.getResourceByUUID(uuid);

        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        InputStream inputStream = null;

        List<Capability> capList = new ArrayList<>();

        try {

            Reader stringReader = new StringReader(ontologyXML);
            reader = factory.createXMLStreamReader(stringReader);

            while (true) {

                int event = reader.next();

                if (event == XMLStreamConstants.END_DOCUMENT) {
                    reader.close();
                    break;
                }

                if (event == XMLStreamConstants.START_ELEMENT) {

                    switch (reader.getLocalName()) {
                        case "InputCapability":
                            Capability inputCap = new Capability();
                            inputCap.setName(XMLUtility.getAttributeValue(reader, "ID"));
                            inputCap.setAccesspolicy(res.getSecuritypolicy());
                            capList.add(inputCap);
                            break;
                        case "ExecutionCapability":
                            Capability exeCap = new Capability();
                            exeCap.setName(XMLUtility.getAttributeValue(reader, "ID"));
                            exeCap.setAccesspolicy(res.getSecuritypolicy());
                            capList.add(exeCap);
                            break;
                    }
                }
            }

        } catch (Exception ex) {
            throw new DirectoryServiceException(ex);
        } finally {
            closeStreams(reader, inputStream);
        }

        dtAccess.addResourceCapabilities(res, capList);

    }

    public void saveCurrentOntologyModelAsFiles() throws Exception {

        OutputStream output = new FileOutputStream("c:\\outputtext.txt");
        RDFDataMgr.write(output, ontoDLWithRulesModel, org.apache.jena.riot.RDFFormat.RDFXML_ABBREV);

    }
}
