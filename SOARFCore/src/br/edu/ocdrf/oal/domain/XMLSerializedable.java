package br.edu.ocdrf.oal.domain;

import br.edu.ocdrf.util.xml.ObjectStringConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public abstract class XMLSerializedable {

    protected XStream ontologyXStreamModel;

    public XMLSerializedable() {
        defineXStreamParser();
    }

    public final void defineXStreamParser() {

        ontologyXStreamModel = new XStream(new DomDriver());
        ontologyXStreamModel.registerConverter(new ObjectStringConverter());
        ontologyXStreamModel.omitField(XMLSerializedable.class, "ontologyXStreamModel");
        setXmlModel(ontologyXStreamModel);

    }

    public String parseThisToXML() {
        return ontologyXStreamModel.toXML(this);
    }

    public abstract Object createObjectFromXML(String xml);

    protected abstract void setXmlModel(XStream xstream);

}
