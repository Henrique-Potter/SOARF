package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.message.CryptoMessages;
import com.fasterxml.jackson.databind.JavaType;

public class ServiceRegistrationReq extends CryptoMessages<ServiceRegistrationData> {

    public String encryptedRequestData;

    public void setEncryptedServiceData(ServiceRegistrationData servRegData, String adminPassword) throws Exception {

        encryptedRequestData = mapDataAsJsonAndEncrypt(servRegData, adminPassword, true);

    }

    public ServiceRegistrationData getEncryptedServiceData(String adminPassword) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(ServiceRegistrationData.class);
        ServiceRegistrationData data = getDataField(adminPassword, encryptedRequestData, type, true);

        return data;
    }

}
