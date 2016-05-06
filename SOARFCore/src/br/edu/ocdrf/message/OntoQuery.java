package br.edu.ocdrf.message;

import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.util.AES;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OntoQuery {

    public String queryData;

    public String encryptedJsonDirectoryServTicket;

    public void setEncryptedJsonDirectoryServTicket(String encryptedJsonDirectoryServTicket) {
        this.encryptedJsonDirectoryServTicket = encryptedJsonDirectoryServTicket;
    }

    public ServiceTicket getDirServTicket(String dirSecretKey) {

        ServiceTicket serviceTicket = null;
        try {

            AES aes = new AES();
            aes.setSecretKey(dirSecretKey);

            String servTicket = aes.decrypt(encryptedJsonDirectoryServTicket);

            ObjectMapper mapper = new ObjectMapper();

            serviceTicket = mapper.readValue(servTicket, ServiceTicket.class);

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return serviceTicket;
    }

    public QueryData getQueryData(String sessionKey) {

        QueryData qData = null;

        try {

            AES aes = new AES();
            aes.setSecretKey(sessionKey);

            String decryptedQData = aes.decrypt(queryData);

            ObjectMapper mapper = new ObjectMapper();

            qData = mapper.readValue(decryptedQData, QueryData.class);

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return qData;
    }

    public void setQueryDataAndEncrypt(QueryData qData, String sessionKey) {

        try {

            queryData = mapAsJsonAndEncrypt(qData, sessionKey);

        } catch (Exception ex) {
            System.err.println(ex);
        }

    }

    private String mapAsJsonAndEncrypt(QueryData qData, String sessionKey) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(qData);

        AES aes = new AES();
        aes.setSecretKey(sessionKey);

        return aes.encrypt(temp);
    }

}
