package br.edu.ocdrf.util;

import br.edu.ocdrf.message.UserCertificate;
import br.edu.ocdrf.message.ResourceRegistrationRequest;
import br.edu.ocdrf.util.AES;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.spi.http.HttpExchange;

public class RequestAuth {

    public static boolean authenticateMessage(ResourceRegistrationRequest resRegReq, String password, String outMessage, WebServiceContext wsContext) {

        ObjectMapper mapper = new ObjectMapper();
        boolean messageAuthStatus = false;

        try {

            String cryptoMessage = resRegReq.jsonCryptoMessageSource;
            String md5password = passwordToMD5(password);
            String clearJsonMessage = decryptMessage(md5password, cryptoMessage);

            UserCertificate cryptoMsg = mapper.readValue(clearJsonMessage, UserCertificate.class);

            if (getMessageSenderRemoteHost(wsContext).equals(cryptoMsg.userLocalAddress)) {

                Date today = new Date();

                long requestElapsedTime = cryptoMsg.requestGenerationDate - today.getTime();

                //se mensagem for mais velha que 1 hora invalida requisitção.
                //TODO melhorar
                if (requestElapsedTime <= 3600000) {
                    messageAuthStatus = true;
                }
            }

        } catch (Exception e) {
            System.err.println(e);
        }
        return messageAuthStatus;
    }

    private static String getMessageSenderRemoteHost(WebServiceContext wsContext) {

        MessageContext msgx = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) msgx.get("com.sun.xml.ws.http.exchange");

        InetSocketAddress remoteAddress = exchange.getRemoteAddress();
        String remoteHost = remoteAddress.getHostName();

        System.out.println("Client IP = " + remoteHost);
        return remoteHost;
    }

    public static String decryptMessage(String md5Password, String message) {
        String decryptedCipherText = null;
        try {

            AES aes = new AES();
            aes.setSecretKey(md5Password);

            decryptedCipherText = aes.decrypt(message);

        } catch (Exception ex) {
            System.err.printf("");
        }
        return decryptedCipherText;
    }

    public static String encryptMessage(String password, String message) {
        String ecryptedCipherText = null;
        try {

            AES aes = new AES();
            aes.setSecretKey(passwordToMD5(password));

            ecryptedCipherText = aes.encrypt(message);

        } catch (Exception ex) {
            System.err.printf("");
        }
        return ecryptedCipherText;
    }

    private static String passwordToMD5(String password) {

        String md5 = null;

        if (null == password) {
            return null;
        }

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(password.getBytes(), 0, password.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }

}
