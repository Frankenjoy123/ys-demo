package com.yunsoo.third.dao.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * Created by yan on 11/10/2016.
 */
public class XmlUtils {

    public static <T> T convertXmlToObject(String xml, Class... classType) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classType);
        Unmarshaller unMarshaller = context.createUnmarshaller();
        T resultObj = (T)unMarshaller.unmarshal(new StreamSource(new StringReader(xml)));
        return resultObj;
    }
}
