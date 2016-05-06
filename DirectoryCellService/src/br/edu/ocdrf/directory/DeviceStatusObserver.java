package br.edu.ocdrf.directory;

import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.OAOResource;
import br.edu.ocdrf.oal.domain.OInvokeMethod;
import br.edu.ocdrf.oal.domain.OResourceEntity;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceStatusObserver extends TimerTask {

    protected final Dataset reasonerModel;

    public DeviceStatusObserver(Dataset iModel) {

        reasonerModel = iModel;

    }

    @Override
    public void run() {
        int responseCode = 0;

        List<OResourceEntity> resList = null;
        try {
            reasonerModel.begin(ReadWrite.READ);
            Model model = reasonerModel.getDefaultModel();
            resList = new OAOResource(model).getResourcesInvokeStrings();
            reasonerModel.end();
            model.close();
        } catch (OALException oALException) {
            System.err.println("Error in Device StatusObserver: \n" + oALException);
        }

        for (OResourceEntity res : resList) {
            for (OInvokeMethod inv : res.getInvokeMethod()) {
                try {
                    responseCode = checkWebService(inv.getInvokeString());
                    if (responseCode != 200) {
                        removeDeviceAndSetErrorMessage(null, responseCode, res, inv);
                    }
                } catch (Exception ex) {
                    removeDeviceAndSetErrorMessage(ex, responseCode, res, inv);
                }
            }
        }
    }

    private int checkWebService(String stringUrl) throws Exception {
        URL url = new URL(stringUrl);

        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        huc.connect();

        return huc.getResponseCode();
    }

    private void removeDeviceAndSetErrorMessage(Exception ex, int responseCode, OResourceEntity res, OInvokeMethod inv) {

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Error connecting with :").append(res.getNodeId())
                .append("\nConnection String used was :").append(inv.getInvokeString())
                .append("\nHTTP Server responded :");

        if (ex != null) {
            errorMessage.append(ex.getMessage());

        } else {
            errorMessage.append("Http Error code ")
                    .append(responseCode);
        }

        errorMessage.append("\nNon responsive resource will be removed.");

        System.err.println(errorMessage.toString());

        try {
            reasonerModel.begin(ReadWrite.WRITE);
            Model model = reasonerModel.getDefaultModel();

            new OAOResource(model).deleteResourceById(res.getNodeId());
            reasonerModel.commit();
            reasonerModel.end();
            model.close();

        } catch (Exception exception) {
            System.err.println(exception);
        }
        System.err.println("\nResource succesfully removed!\n\n");

        Logger.getLogger(DeviceStatusObserver.class.getName()).log(Level.SEVERE, errorMessage.toString(), ex);
    }
}
