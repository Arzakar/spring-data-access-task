package com.rntgroup.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MarshallerWrapper {

    Jaxb2Marshaller jaxb2Marshaller;

    public <T> String marshall(T object) {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        jaxb2Marshaller.marshal(object, result);
        return sw.toString();
    }

    public <T> T unmarshall(InputStream xmlFile, Class<?> xmlClass) {
        jaxb2Marshaller.setMappedClass(xmlClass);
        return (T) jaxb2Marshaller.unmarshal(new StreamSource(xmlFile));
    }
}
