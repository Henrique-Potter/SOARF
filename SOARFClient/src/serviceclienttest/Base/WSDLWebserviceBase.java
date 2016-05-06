package serviceclienttest.Base;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WSDLWebserviceBase {

    //protected String baseAddress = "http://152.92.155.231";
    protected String baseAddress = "http://192.168.0.10";
    protected String serviceAddress;

    protected final ObjectMapper jsonMapper = new ObjectMapper();

    protected void setServiceAddress(String sAddress) {
        serviceAddress = baseAddress + sAddress;
    }

}
