package com.experian.common.helpers;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.w3c.dom.Document;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public abstract class APIOperations {

    protected String url;

    //For testing purposes set  proxyHostName and proxyPort in order to sniff http requests
    private final String proxyHostName = "";
    private final Integer proxyPort = -1;


    public APIOperations() {

        initClient();
    }

    public static String removeXmlNamespaces(String response) {
        String s = response.replaceAll("xmlns(:[a-zA-Z0-9]+)?=(\"[^\"]+\"|\'[^\']+\')", "");
        s = s.replaceAll("(</?)[a-zA-Z0-9]+:", "$1");
        return s.replaceAll("xsi:(type|nil)=(\"[^\"]+\"|'[^']+')", "");
    }

    public String documentToString(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(domSource, result);
        return writer.toString();
    }

    private void initClient() {
        if (StringUtils.isNotEmpty(proxyHostName) && proxyPort > 0) {
            Unirest.setProxy(new HttpHost(proxyHostName, proxyPort));
        }
        Unirest.clearDefaultHeaders();
    }

    public String getUrl() {
        return url;
    }

}