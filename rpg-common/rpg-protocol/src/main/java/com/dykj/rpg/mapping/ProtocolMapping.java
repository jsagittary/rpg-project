package com.dykj.rpg.mapping;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProtocolMapping {

    private static Map<Integer,Class> classMap;

    static{

        classMap = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = ProtocolMapping.class.getClassLoader().getResourceAsStream("java/protocol-mapping.xml");
            Document doc = builder.parse(is);
            NodeList protocols = doc.getElementsByTagName("protocol");
            if(protocols != null){
                for(int i=0;i<protocols.getLength();i++){
                    NamedNodeMap nodes = protocols.item(i).getAttributes();
                    int id = Integer.parseInt(nodes.getNamedItem("id").getNodeValue());
                    String clazzString = nodes.getNamedItem("protocolClass").getNodeValue();

                    classMap.put(id,Class.forName(clazzString));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Class getProtocolClassByCode(int code){
        return classMap.get(code);
    }

    public static Map<Integer,Class> getClassMap(){
        return classMap;
    }

}
