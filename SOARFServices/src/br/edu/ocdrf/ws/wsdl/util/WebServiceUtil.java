/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.ws.wsdl.util;

import java.net.URL;

/**
 *
 * @author Enuma
 */
public abstract class WebServiceUtil {

    protected URL serviceURL;
    protected URL serviceWSDLURL;

    public WebServiceUtil() {

    }

    public URL getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(URL url) {
        serviceURL = url;
    }

    public URL getServiceWSDLURL() {
        return serviceWSDLURL;
    }

    public void setServiceWSDLURL(URL wsdlurl) {
        serviceWSDLURL = wsdlurl;
    }
}
