package br.edu.ocdrf.util.xml;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class ObjectStringConverter implements SingleValueConverter {

    @Override
    public String toString(Object obj) {
        return obj.toString();
    }

    @Override
    public Object fromString(String name) {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Object.class);
    }
}
