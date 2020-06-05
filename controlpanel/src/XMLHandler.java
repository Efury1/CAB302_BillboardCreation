import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;

public class XMLHandler {
    public static void main(String[] args)
    {
        //  Read the XML document
        Document document = ImportXMLFromFile("C:/Users/Ethan/IdeaProjects/CAB302-Assignment1/billboards/10.xml");
        if (ValidateDoc(document))
        {
            //  Split the file into its constituent parts
            ParseXMLData(document.getDocumentElement());
        }
        else
        {
            System.err.println("Invalid XML format or structure.");
        }
    }


    /**
     * Validates the document to the specification's format. (Format: UTF-8 and Version: 1.0)
     * @param document A document to check against the validation methods.
     * @return True if the document is in the correct format according to the specification.
     */
    private static Boolean ValidateDoc(Document document)
    {
        Boolean valid = true;
        if (!document.getInputEncoding().equals("UTF-8"))
        {
            valid = false;
        }
        if (!document.getXmlVersion().equals("1.0"))
        {
            valid = false;
        }
        return valid;
    }


    /**
     * Imports an XML file and stores it in memory as a document tree to be used by other XMLHandler methods.
     * @param directoryOfXML Directory of the input file.
     * @return A document tree of type Document.
     */
    public static Document ImportXMLFromFile(String directoryOfXML)
    {
        //  Read the file into memory
        File input = new File(directoryOfXML);
        //  Document builder factory api used to parse xml tags
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("1Invalid XML format or structure.");
            e.printStackTrace();
        }

        Document document = null;
        try {
            document = documentBuilder.parse(directoryOfXML);
        } catch (SAXException e) {
            System.err.println("2Invalid XML format or structure.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unsupported encoding.");
        }

        //  Normalise the document data (eg. https://stackoverflow.com/a/13787629)
        document.getDocumentElement().normalize();

        return document;
    }

    //  Exports the structured XML to a specified file/directory
    public static void SaveXMLToFile(String directoryOfXML)
    {
        //  doSomething();
    }

    //  Parse XML document read from ImportXMLFromFile()

    /**
     * Traverses over a node's child elements and extracts the 6 return values in a string array.
     * @param node Billboard node to traverse over.
     * @return A string array in the format:
     * 0 - billboardColour
     * 1 - message
     * 2 - messageColour
     * 3 - information
     * 4 - informationColour
     * 5 - pictureData
     */
    public static String[] ParseXMLData(Node node)
    {
        String[] exportData = new String[6];

        //  billboard colour
        try {
            exportData[0] = node.getAttributes().getNamedItem("colour").getNodeValue().substring(1, 7);
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            System.err.println("No billboard colour found.");
        }
        //  iterate over each child element
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentNode;
                String currentNodeName = element.getNodeName();
                if (currentNodeName == "message")
                {
                    //  message
                    exportData[1] = element.getTextContent();
                    if (element.hasAttributes())
                    {
                        //   message colour
                        try {
                            exportData[2] = element.getAttributes().getNamedItem("colour").getNodeValue().substring(1, 7);
                        } catch (StringIndexOutOfBoundsException e) {
                            System.err.println("XML: No message colour found.");
                        }
                    }
                }
                else if (currentNodeName == "picture")
                {
                    //  picture data
                    if (element.hasAttributes())
                    {
                        try {
                            exportData[5] = element.getAttributes().getNamedItem("data").getNodeValue();
                        } catch (NullPointerException e) {
                            System.err.println("XML: No picture URL found.");
                        }
                        try {
                            exportData[5] = element.getAttributes().getNamedItem("url").getNodeValue();
                        } catch (NullPointerException e) {
                            System.err.println("XML: No picture data found.");
                        }
                    }
                }
                else if (currentNodeName == "information")
                {
                    //  information
                    exportData[3] = element.getTextContent();
                    if (element.hasAttributes())
                    {
                        //  information colour
                        try {
                            exportData[4] = element.getAttributes().getNamedItem("colour").getNodeValue().substring(1, 7);
                        } catch (StringIndexOutOfBoundsException e) {
                            System.err.println("XML: No information colour found.");
                        }
                    }
                }
            }
        }
        //  check if the colours are valid hex strings
        for (int i = 0; i < 6; i+=2)
        {
            if (exportData[i] != null)
            {
                //  regex from https://stackoverflow.com/a/11424571
                if (!exportData[i].matches("-?[0-9a-fA-F]+"))
                {
                    exportData[i] = null;
                }
            }
        }
        return exportData;
    }   //  end ParseXMLData()


    //  I don't think this is needed but idk
    public static void GetDataFromServer()
    {
        //  doSomething();
    }
}

























