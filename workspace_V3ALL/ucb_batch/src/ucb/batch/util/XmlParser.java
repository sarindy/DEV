package ucb.batch.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlParser {
    
    private static final Logger logger = Logger.getLogger(XmlParser.class);
    
    private String xmlFileName = "";
    private String xmlBeanId = "";
    
    public XmlParser(String fileName, String beanId){
        xmlFileName = fileName.trim();
        xmlBeanId = beanId.trim();
        logger.info(super.getClass().toString() + ": XML File name : " + xmlFileName);
        logger.info(super.getClass().toString() + ": XML Bean ID : " + xmlBeanId);
    }
    
    public String getProperty(String propertyName){
        String propertyValue = "";
        boolean findPropertyValue = false;
        try {
            File file = new File(xmlFileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            
            NodeList nodeLst = doc.getElementsByTagName("bean");
            //System.out.println("Length: " + nodeLst.getLength());
    
            for (int s = 0; s < nodeLst.getLength(); s++) {
                if (findPropertyValue) break;
                NodeList ndList  = ((Element)nodeLst.item(s)).getElementsByTagName("property");
                //System.out.println("bean: " + nodeLst.item(s).getNodeName());
                //System.out.println("bean id: " + ((Element)nodeLst.item(s)).getAttribute("id"));                
                //System.out.println("ndList Length: " + ndList.getLength());
                if (xmlBeanId.equalsIgnoreCase(((Element)nodeLst.item(s)).getAttribute("id").trim())){
                    for (int i=0; i < ndList.getLength(); i++){
                        //System.out.println("property: " + ndList.item(i).getNodeName());
                        //System.out.println("property name: " + ((Element)ndList.item(i)).getAttribute("name"));
                        if (propertyName.equalsIgnoreCase(((Element)ndList.item(i)).getAttribute("name"))){
                            propertyValue = (((Element)ndList.item(i)).getElementsByTagName("value")).item(0).getTextContent().trim();  
                            findPropertyValue = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertyValue;
    }
    
    public Map<String, Object> getMap(String propertyName) {
        Map<String, Object> mapValue = new HashMap<String, Object>();
        try {
            File file = new File(xmlFileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
//            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("bean");
//            System.out.println("Length: " + nodeLst.getLength());
    
            for (int s = 0; s < nodeLst.getLength(); s++) {
//            	System.out.println("bean: " + nodeLst.item(s).getNodeName());
//            	System.out.println("bean id: " + ((Element)nodeLst.item(s)).getAttribute("id"));
                if (xmlBeanId.equalsIgnoreCase(((Element)nodeLst.item(s)).getAttribute("id").trim())){
                	NodeList ndList  = ((Element)nodeLst.item(s)).getElementsByTagName("property");
//                	System.out.println("ndList Length: " + ndList.getLength());
                                    
					for (int i = 0; i < ndList.getLength(); i++) {
//                        System.out.println("property: " + ndList.item(i).getNodeName());
//                        System.out.println("property name: " + ((Element)ndList.item(i)).getAttribute("name"));
                        if (propertyName.equalsIgnoreCase(((Element)ndList.item(i)).getAttribute("name"))){
                        	NodeList ndList2  = ((Element)nodeLst.item(s)).getElementsByTagName("entry");
//    						System.out.println("ndList2 Length: " + ndList2.getLength());
							for (int j = 0; j < ndList2.getLength(); j++) {
								Element tEntryElement = (Element)ndList2.item(j);
								String tKey = ((Element)tEntryElement.getElementsByTagName("value").item(0)).getTextContent().trim();
								int tListValueTagCount = tEntryElement.getElementsByTagName("list").getLength();
								
//								System.out.println(j + "\tkey: " + tKey + "\t" + tEntryElement.getElementsByTagName("value").item(1).getTextContent().trim() + "\t" + tListValueTagCount + "\t" + tEntryElement.getElementsByTagName("value").getLength());
								
								if (tListValueTagCount == 0) { // 單一<value>
									mapValue.put(tKey, tEntryElement.getElementsByTagName("value").item(1).getTextContent().trim());
								}else { // <list><value></value></list>
									List<String> tValueList = new ArrayList<String>();
									for (int k = 1 ; k < tEntryElement.getElementsByTagName("value").getLength() ; k++) {
										tValueList.add(tEntryElement.getElementsByTagName("value").item(k).getTextContent().trim());
									}
									mapValue.put(tKey, tValueList);
								}
                        	}
                        }
                    }
                }
            }
//            for (String tKey : mapValue.keySet()) {
//            	System.out.println("++ " + tKey + ": " + mapValue.get(tKey));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapValue;
    }
}
