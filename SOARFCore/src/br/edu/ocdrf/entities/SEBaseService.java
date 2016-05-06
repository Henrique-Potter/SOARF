package br.edu.ocdrf.entities;

import br.edu.ocdrf.auth.message.ServiceRegistrationData;
import br.edu.ocdrf.auth.message.ServiceRegistrationReq;
import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.oal.domain.OResourceEntity;
import br.edu.ocdrf.util.AES;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.mgt.Explain;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public abstract class SEBaseService extends ServiceCore {

    public int urlPortNumber;

    private String appHomePath;

    protected final int CONTEXT_TYPE = 1;
    protected final int DISCOVERY_TYPE = 2;
    protected final int DIRECTORY_TYPE = 0;
    protected final int AGENT_TYPE = 3;

    private static final String AGENTS_CONFIG_BASE_PATH = "/config/agents/";
    private static final String MAIN_SERV_CONFIG_BASE_PATH = "/config/main_services/";
    private static final String DIR_ONTOLOGY_DATA_BASE_PATH = "/config/main_services/ontoData";
    private static final String AGENTS_XSD_CONFIG_BASE_PATH = "/config/agents/xsd/";
    private static final String MAIN_SERV_XSD_CONFIG_BASE_PATH = "/config/main_services/xsd/";
    private static final String REG_CERTIFICATION_BASE_PATH = "/reg_certificate/";
    private static final String LOG_BASE_PATH = "/log/";

    public static final String WEBSERVICE_INV_TECH = "webservice";

    protected static final ObjectMapper jsonMapper = new ObjectMapper();

    @javax.annotation.Resource
    protected WebServiceContext wsContext;
    protected String localServiceIP;

    protected Dataset dataset;

    private static String agentsConfigPath;
    private static String ontologyData;
    private static String mainServicesConfigPath;
    private static String agentsXSDConfigPath;
    private static String mainServicesXSDConfigPath;
    private static String servicesRegCertPath;
    private static String logPath;

    protected abstract void readConfig(File file) throws ServiceBaseException;

    public String getAppHomePath() {
        return appHomePath;
    }

    public String getAgentsConfigPath() {
        return agentsConfigPath;
    }

    public String getMainServicesConfigPath() {
        return mainServicesConfigPath;
    }

    public String getMainServicesXSDConfigPath() {
        return mainServicesXSDConfigPath;
    }

    public static String getAgentsXSDConfigPath() {
        return agentsXSDConfigPath;
    }

    public static String getLogPath() {
        return logPath;
    }

    public SEBaseService(){

    }
    
    public void initServiceBaseProps() throws ServiceBaseException {
        chkEnvVariableHomePath();
        agentsConfigPath = appHomePath + AGENTS_CONFIG_BASE_PATH;
        servicesRegCertPath = appHomePath + REG_CERTIFICATION_BASE_PATH;
        mainServicesConfigPath = appHomePath + MAIN_SERV_CONFIG_BASE_PATH;
        agentsXSDConfigPath = appHomePath + AGENTS_XSD_CONFIG_BASE_PATH;
        mainServicesXSDConfigPath = appHomePath + MAIN_SERV_XSD_CONFIG_BASE_PATH;
        ontologyData = appHomePath + DIR_ONTOLOGY_DATA_BASE_PATH;
        logPath = appHomePath + LOG_BASE_PATH;
        logPath = appHomePath + LOG_BASE_PATH;
        oResource = new OResourceEntity();
        resOntDesc = new ResourceOCDRF();
    }

    //The OS enviorment variable must define the path for the OCDRF home under the name OCDRF_HOME
    private void chkEnvVariableHomePath() throws ServiceBaseException {
        try {
            appHomePath = System.getenv("CONTQUEST_HOME");
        } catch (Exception e) {
            throw new ServiceBaseException(e);
        }
    }

    protected void chkServiceRegistration() throws Exception {

        if (!serviceCertificateName.isEmpty()) {

            File regCertFile = new File(servicesRegCertPath, serviceCertificateName);

            if (regCertFile.exists()) {
                regCert = new RegistrationCertificate().createObjectFromXML(regCertFile);
            } else {
                //requestCertificate
            }

        } else {
            throw new Exception("Empty certificate name.");
        }

    }

    protected void loadOntologyModels(String configPath, String ontologyFileName) throws ServiceBaseException {

        try {
            ARQ.setExecutionLogging(Explain.InfoLevel.NONE);

            String fullOntologyFilePath = configPath + ontologyFileName;

            baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            baseModel.read(fullOntologyFilePath, "RDF/XML");
            // create the reasoning model using the base
            ontologyDLModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF, baseModel);
            // ontoDLWithRulesModel = ModelFactory.createRDFSModel(ontologyDLModel);
        } catch (Exception e) {
            throw new ServiceBaseException(e);
        }

    }

    protected void loadOntologyModelsWithRules(String ontologyFileName, String rulesFileName) throws ServiceBaseException {

        try {

            ARQ.setExecutionLogging(Explain.InfoLevel.NONE);

            //String fullOntologyFilePath = getMainServicesConfigPath() + ontologyFileName;
            Path input = Paths.get(getMainServicesConfigPath(), ontologyFileName);

            dataset = TDBFactory.createDataset(ontologyData);

            baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            baseModel.read(input.toUri().toString(), "RDF/XML");

            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();
            model.add(baseModel);
            dataset.commit();
            dataset.end();
            model.close();
            baseModel.close();

            // create the reasoning model using the base
            //ontologyDLModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF, baseModel);
            //List<Rule> rules = Rule.rulesFromURL("file:/" + getMainServicesConfigPath() + rulesFileName);
            //Reasoner r1 = new GenericRuleReasoner(rules);
            //r1.setDerivationLogging(true);
            //ontoDLWithRulesModel = ModelFactory.createInfModel(r1, ontologyDLModel);
        } catch (Exception e) {
            throw new ServiceBaseException(e);
        }
    }

    protected String readOntologyFile(String mainServicesConfigPath, String ontologyFileName) throws Exception {
        String ontXML = "";
        StringBuilder ontologyString = new StringBuilder();
        FileInputStream in = null;
        BufferedReader input = null;

        if (ontologyFileName == null || ontologyFileName.trim().isEmpty()) {
            throw new ServiceBaseException("Ontology file name not set");
        }

        try {

            String SOURCE = mainServicesConfigPath + ontologyFileName;
            System.out.println("Resource.readOntology - " + SOURCE);

            in = new FileInputStream(new File(SOURCE));
            input = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String aux = "";

            String onlyOnce = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
            boolean once = true;

            while ((aux = input.readLine()) != null) {
                if (once) {
                    ontologyString.append(onlyOnce);
                    once = false;
                } else {
                    ontologyString.append(aux);
                }
            }

            ontXML = changeURLPort(ontologyString.toString());
            ByteArrayInputStream inputStreamChanged = new ByteArrayInputStream(ontXML.getBytes());
            loadOntologyFromInputStream(inputStreamChanged);

            inputStreamChanged.close();

        } catch (Exception e) {
            throw new ServiceBaseException(e);
        } finally {
            input.close();
            in.close();
        }

        return ontXML;
    }

    private void loadOntologyFromInputStream(ByteArrayInputStream inputStream) throws Exception {

        
        baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        baseModel.read(inputStream, "RDF/XML");
        //ontologyDLModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF, baseModel);


    }

    protected String getMessageSenderRemoteHost(WebServiceContext wsContext) {

        MessageContext msgx = wsContext.getMessageContext();

        HttpExchange exchange = (HttpExchange) msgx.get("com.sun.xml.internal.ws.http.exchange");

        InetSocketAddress remoteAddress = exchange.getRemoteAddress();
        InetAddress address = remoteAddress.getAddress();
        String clientIp = address.getHostAddress();

        return clientIp;

    }

    protected boolean verifyLocationAndTime(WebServiceContext wsContext, String ticketClaimedLocation, long ticketValidity) {

        boolean isValid = false;

        if (ticketClaimedLocation.equals(getMessageSenderRemoteHost(wsContext))) {
            isValid = true;
        }
        if (System.currentTimeMillis() <= ticketValidity) {
            isValid = false;
        }

        return isValid;
    }

    protected void closeStreams(XMLStreamReader reader, InputStream inputStream) throws ServiceBaseException {

        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new ServiceBaseException(e);
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ServiceBaseException(e);
            }
        }
    }

    protected void setNewServiceCrendentials(String encryptedJsonServID, String userPassword) throws Exception {

        AES aes = new AES();
        aes.setKeyFromHashedPassword(userPassword);

        ServiceID fullServID = jsonMapper.readValue(aes.decrypt(encryptedJsonServID), ServiceID.class);

        servID.uuid = fullServID.uuid;
        servID.secretKey = fullServID.secretKey;
        servID.contextURL = fullServID.contextURL;
        servID.directorySessionKey = fullServID.directorySessionKey;
        servID.contextSessionKey = fullServID.contextSessionKey;
        servID.dirServiceTicket = fullServID.dirServiceTicket;
        servID.contextServiceTicket = fullServID.contextServiceTicket;

    }

    protected String createServiceRegistrationReq(int serviceType, String serviceURL, String userID, String userPassword, String localServiceIP) throws Exception {

        ServiceRegistrationReq serRegReq = new ServiceRegistrationReq();

        ServiceRegistrationData servRegDt = new ServiceRegistrationData();
        servRegDt.creationDate = System.currentTimeMillis();
        servRegDt.localAddress = localServiceIP;
        servRegDt.serviceType = serviceType;
        servRegDt.serviceUrl = serviceURL;
        servRegDt.requestingClientID = userID;
        servRegDt.ticketOwnerID = userID;

        serRegReq.requesterClaimedID = userID;
        serRegReq.setEncryptedServiceData(servRegDt, userPassword);

        return jsonMapper.writeValueAsString(serRegReq);

    }

    protected boolean validateServiceTicket(ServiceTicket servTicket, String ticketOwnerID, String clientIP) {

        boolean validServiceTicket = true;

        if (!servTicket.targetServiceUUID.equals(servID.uuid)) {
            validServiceTicket = false;
        }
        if (!(servTicket.clientID.equals(ticketOwnerID) || servTicket.ownerServiceUUID.equals(ticketOwnerID))) {
            validServiceTicket = false;
        }
        if ((servTicket.validity + servTicket.creationDate) <= System.currentTimeMillis()) {
            validServiceTicket = false;
        }
        if (!servTicket.clientLocalAddress.equals(clientIP)) {
            validServiceTicket = false;
        }

        return validServiceTicket;
    }

    protected boolean validateClaimedNameAndLocation(String claimedID, String decryptedID, String claimedIP, String requesterIP) {

        boolean validServiceTicket = true;

        if (!claimedID.equals(decryptedID)) {
            validServiceTicket = false;
        }
        if (!claimedIP.equals(requesterIP)) {
            validServiceTicket = false;
        }

        return validServiceTicket;
    }

    public String changeURLPort(String xml) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc;
        StringReader str = new StringReader(xml);
        InputSource is = new InputSource();
        is.setCharacterStream(str);
        is.setEncoding("UTF-8");

        doc = builder.parse(is);

        str.close();
        NodeList nodeList = doc.getElementsByTagName("invokeString");

        String urlBegin = "http://192.168.1.100:";
        String urlEnd = "/IrisNetworkWS?wsdl";

        String fullUrl = urlBegin + urlPortNumber + urlEnd;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            node.setTextContent(fullUrl);
        }

        return docToString(doc);
    }

    public String docToString(Document doc) {
        try {

            StringWriter sw = new StringWriter();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            StreamResult str = new StreamResult();

            transformer.transform(new DOMSource(doc), new StreamResult(sw));

            String output = sw.getBuffer().toString();

            return output;
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

}
