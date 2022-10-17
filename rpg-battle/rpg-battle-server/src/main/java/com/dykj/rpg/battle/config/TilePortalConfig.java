package com.dykj.rpg.battle.config;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TilePortalConfig {
    public int id;
    public String mapPath;
    public TilePortalDetail[] portalDetails;

    public TilePortalConfig(int id,String mapPath){
        this.id = id;
        this.mapPath = mapPath;
        portalDetails = new TilePortalDetail[6];
        loadConfig();
    }

    private void loadConfig(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = TilePortalConfig.class.getClassLoader().getResourceAsStream(mapPath);
            Document doc = builder.parse(is);
            NodeList region = doc.getElementsByTagName("Region");
            if(region != null){
                loadRegion(region);
            }
            NodeList portal = doc.getElementsByTagName("Portal");
            if(portal != null){
                loadPortal(portal);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadRegion(NodeList region){
        for(int i=0;i<region.getLength();i++){
            NamedNodeMap map = region.item(i).getAttributes();

            String type = map.getNamedItem("type").getNodeValue();
            String x = map.getNamedItem("x").getNodeValue();
            String z = map.getNamedItem("z").getNodeValue();
            String width = map.getNamedItem("width").getNodeValue();
            String height = map.getNamedItem("height").getNodeValue();
        }
    }

    public void loadPortal(NodeList portal){
        for(int i=0;i<portal.getLength();i++){
            NamedNodeMap map = portal.item(i).getAttributes();

            String id = map.getNamedItem("id").getNodeValue();
            String type = map.getNamedItem("type").getNodeValue();
            String x = map.getNamedItem("x").getNodeValue();
            String z = map.getNamedItem("z").getNodeValue();

            portalDetails[Integer.parseInt(id)-1] = new TilePortalDetail(Integer.parseInt(id),type,Float.parseFloat(x),Float.parseFloat(z));
        }
    }

}

