package serviceclienttest.clients;

import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class RDFXMLOntologyLoader {

    public static String getOntologyAsString() throws DirectoryServiceException, ServiceBaseException {

        try {

            String filepath = "c:\\OCDRF\\config\\agents\\IrisNetworkOntology.owl";

            //FileInputStream in = new FileInputStream(new File(filepath));
            //String theString = readFile(filepath, StandardCharsets.UTF_8);
            String theString = rFile(filepath);

            //in.close();
            return theString;

        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    public static String rFile(String filepath) throws Exception {

        FileInputStream in = new FileInputStream(new File(filepath));
        BufferedReader input = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        StringBuilder builder = new StringBuilder();
        String aux = "";

        String onlyOnce = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
        boolean once = true;

        while ((aux = input.readLine()) != null) {
            if (once) {
                builder.append(onlyOnce);
                once = false;
            } else {
                builder.append(aux);
            }

        }
        in.close();
        input.close();

        return builder.toString();
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));

        return builder.parse(is);
    }

    static String convertStreamToString(java.io.InputStream is) {

        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";

    }

}
