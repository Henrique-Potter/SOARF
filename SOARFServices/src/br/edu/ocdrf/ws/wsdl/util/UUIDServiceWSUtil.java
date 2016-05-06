package br.edu.ocdrf.ws.wsdl.util;

import br.edu.ocdrf.exceptions.DirectoryServiceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class UUIDServiceWSUtil {

    private final String uri = "http://152.92.236.18:8080/UUIDServer/webresources/uuidgen?";
    private int connectionAttempts = 1;
    private final int maxAttempt = 3;

    private static final Logger log = Logger.getLogger(UUIDServiceWSUtil.class.getName());

    public String getNewUUID() throws DirectoryServiceException,IOException {
        Random random = new Random();
        HttpURLConnection connection = null;
        String newUUID = "";

        try {

            while (true) {
                try {

                    URL url = new URL(uri);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    newUUID = convertStreamToString(connection.getInputStream());
                    break;

                } catch (IOException iOException) {
                    if (connectionAttempts >= maxAttempt) {
                        log.error("UUID server have not responded upon " + maxAttempt + " attempts.", iOException);
                        throw iOException;
                    }
                    log.error("UUID server not responding. Attempt number: " + connectionAttempts, iOException);
                    connectionAttempts++;
                    log.info("Initiating attempt number: " + connectionAttempts);
                    try {
                        Thread.sleep(random.nextInt(5) * 1000);
                    } catch (InterruptedException e) {
                        log.error(e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
            log.info("UUID server responded upon " + connectionAttempts + " attempt(s).");

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return newUUID;
    }

    private String convertStreamToString(java.io.InputStream is) {

        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
