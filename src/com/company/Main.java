package com.company;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    private static String AF_DIR = "c:\\output_dir\\af\\";
    private static String FORMS_AND_DOCS_DIR = "c:\\output_dir\\formsanddocuments\\";
    private static String OUTPUT_FILE_NAME = ".content.xml";


    private static String AF_HEADER = "af header";
    private static String FORMS_AND_DOCS_FILE = "forms and docs file";


    public static void main(String[] args) {
        Map <String,Map> fieldMap = new HashMap<String, Map>();
        Map <String,String> attrs = new HashMap<String, String>();;

        attrs.put("name","name");
        attrs.put("componentType","guidetextbox");
        fieldMap.put("test1",attrs);
        fieldMap.put("test2",attrs);


        Element fieldsXML = createFieldsXML(fieldMap);
        //String fieldStr = convertXMLToString(fieldsXML);


        createAFDoc(AF_DIR,fieldsXML);
        createFormsAndDocsFile(FORMS_AND_DOCS_DIR,"testformname");
    }

    private static String convertXMLToString(Element fieldsXML) {
        String xmlString = "";
        TransformerFactory tf = TransformerFactory.newInstance();

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(fieldsXML);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);

            xmlString = result.getWriter().toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return xmlString;
    }

    private static Element createFieldsXML(Map fieldMap){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("items");

        rootElement.setAttribute("jcr:primaryType","nt:unstructured");
        rootElement.setAttribute("sling:resourceType","fd/af/layouts/gridFluidLayout");
        doc.appendChild(rootElement);

        Iterator it = fieldMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Map <String,Map> component = new HashMap<String, Map>();
            component = (Map<String, Map>) pair.getValue();
            Element newComponent = createComponentElement(doc,rootElement,component);
            if (newComponent != null){
                rootElement = newComponent;
            }
        }


        return rootElement;
    }

    private static Element createComponentElement(Document doc, Element rootElement, Map component){

        String fieldName = (String) component.get("name");
        String componentType = (String) component.get("componentType");

        if (componentType == null || fieldName == null) {
            return null;
        }
        // field elements
        Element field = doc.createElement(componentType + "_" + fieldName);
        rootElement.appendChild(field);

        // set attribute to field element
        Attr attr = doc.createAttribute("jcr:created");
        attr.setValue("{Date}2017-01-01T00:00:01.000+01:00");
        field.setAttributeNode(attr);

        attr = doc.createAttribute("jcr:createdBy");
        attr.setValue("admin");
        field.setAttributeNode(attr);

        attr = doc.createAttribute("jcr:primaryType");
        attr.setValue("nt:unstructured");
        field.setAttributeNode(attr);

        attr = doc.createAttribute("textIsRich");
        attr.setValue("[true,true]");
        field.setAttributeNode(attr);

        attr = doc.createAttribute("sling:resourceType");
        attr.setValue("gdocs/components/forms/" + componentType);
        field.setAttributeNode(attr);

        attr = doc.createAttribute(fieldName);
        attr.setValue(fieldName);
        field.setAttributeNode(attr);

        return rootElement;
    }



    private static void createAFDoc(String location, Element fields){
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jcr:root xmlns:sling=\"http://sling.apache.org/jcr/sling/1.0\" xmlns:cq=\"http://www.day.com/jcr/cq/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\"\n" +
                "    jcr:primaryType=\"cq:Page\">\n" +
                "    <jcr:content\n" +
                "        cq:designPath=\"/etc/designs/fd/af/default\"\n" +
                "        cq:deviceGroups=\"/etc/mobile/groups/responsive\"\n" +
                "        cq:lastModified=\"{Date}2017-08-04T11:26:41.126+01:00\"\n" +
                "        cq:lastModifiedBy=\"admin\"\n" +
                "        cq:template=\"/libs/fd/af/templateForFragment/defaultFragmentTemplate\"\n" +
                "        jcr:primaryType=\"cq:PageContent\"\n" +
                "        jcr:title=\"entity\"\n" +
                "        sling:resourceType=\"fd/af/components/page/basePageForFragment\">\n" +
                "        <guideContainer\n" +
                "            jcr:primaryType=\"nt:unstructured\"\n" +
                "            sling:resourceType=\"fd/af/components/guideFragmentContainer\"\n" +
                "            guideCss=\"guideContainer\"\n" +
                "            guideNodeClass=\"guideContainerNode\"\n" +
                "            name=\"fragment1\">\n" +
                "            <layout\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                sling:resourceType=\"fd/af/layouts/defaultGuideLayout\"\n" +
                "                toolbarPosition=\"Bottom\"/>\n" +
                "            <rootPanel\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                sling:resourceType=\"fd/af/components/rootPanel\"\n" +
                "                guideNodeClass=\"rootPanelNode\"\n" +
                "                name=\"guideRootPanel\"\n" +
                "                panelSetType=\"Navigable\">\n" +
                "                <layout\n" +
                "                    jcr:primaryType=\"nt:unstructured\"\n" +
                "                    sling:resourceType=\"fd/af/layouts/gridFluidLayout\"\n" +
                "                    nonNavigable=\"true\"\n" +
                "                    toolbarPosition=\"Bottom\"/>\n" +
                "            </rootPanel>\n" +
                "        </guideContainer>\n" +
                "        <cq:responsive jcr:primaryType=\"nt:unstructured\">\n" +
                "            <breakpoints jcr:primaryType=\"nt:unstructured\">\n" +
                "                <smallScreen\n" +
                "                    jcr:primaryType=\"nt:unstructured\"\n" +
                "                    title=\"Smaller Screen\"\n" +
                "                    width=\"{Decimal}479\"/>\n" +
                "                <phone\n" +
                "                    jcr:primaryType=\"nt:unstructured\"\n" +
                "                    title=\"Phone\"\n" +
                "                    width=\"{Decimal}767\"/>\n" +
                "                <tablet\n" +
                "                    jcr:primaryType=\"nt:unstructured\"\n" +
                "                    title=\"Tablet\"\n" +
                "                    width=\"{Decimal}991\"/>\n" +
                "            </breakpoints>\n" +
                "        </cq:responsive>\n" +
                "    </jcr:content>\n" +
                "</jcr:root>";
        Document doc = convertStringToDocument(xmlStr);



        Node firstDocImportedNode = doc.importNode(fields, true);
        NodeList itemNodes = doc.getElementsByTagName("rootPanel");
        Element items = (Element) itemNodes.item(0);
        items.appendChild(firstDocImportedNode );



        String fileContent = convertXMLToString(doc.getDocumentElement());

        writeFile(location,fileContent);
    }



    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static void createFormsAndDocsFile(String location, String title){
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jcr:root xmlns:sling=\"http://sling.apache.org/jcr/sling/1.0\" xmlns:dam=\"http://www.day.com/dam/1.0\" xmlns:cq=\"http://www.day.com/jcr/cq/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\"\n" +
                "    jcr:primaryType=\"dam:Asset\">\n" +
                "    <jcr:content\n" +
                "        cq:lastReplicated=\"{Date}2017-06-22T16:52:19.810+01:00\"\n" +
                "        cq:lastReplicatedBy=\"admin\"\n" +
                "        cq:lastReplicationAction=\"Activate\"\n" +
                "        jcr:lastModified=\"{Date}2017-07-27T15:01:45.013+01:00\"\n" +
                "        jcr:mixinTypes=\"[cq:ReplicationStatus]\"\n" +
                "        jcr:primaryType=\"dam:AssetContent\"\n" +
                "        sling:resourceType=\"fd/fm/af/render\"\n" +
                "        guide=\"1\">\n" +
                "        <metadata\n" +
                "            jcr:primaryType=\"nt:unstructured\"\n" +
                "            allowedRenderFormat=\"HTML\"\n" +
                "            author=\"admin\"\n" +
                "            availableInMobileApp=\"{Boolean}false\"\n" +
                "            dorType=\"none\"\n" +
                "            formmodel=\"none\"\n" +
                "            title=\" " + title + "\"/>\n" +
                "    </jcr:content>\n" +
                "</jcr:root>\n";

        writeFile(location,fileContent);
    }



    private static void writeFile(String location, String fileContents){
        String fullFileName =  location + OUTPUT_FILE_NAME;

        File file = new File(fullFileName);
        file.getParentFile().mkdirs();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter( location + OUTPUT_FILE_NAME, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writer.println(fileContents);
        writer.close();
    }


}
