//Taken from various sources, including: http://labe.felk.cvut.cz/~xfaigl/mep/xml/java-xml.htm

import java.net.*;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class urlreader1 {
    private  String target = "c:/example2.xml";
    public static void main(String[] args) throws Exception {
        URL yahoo = new URL("http://www.computing.northampton.ac.uk/~scott/csy3025/sample1.html");
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yahoo.openStream()));

        String inputLine;
        String first="a";
        String second="a";
        String third="a";


        while ((inputLine = in.readLine()) != null)//reads in the html page line by line
        {
            String inputLine2=inputLine.replaceAll("\"", "");//removes speechmarks from text
            String[] parts = inputLine2.split("<span itemprop=");//splits into sections based on it is after <span class="
            for (int loop=0;loop<parts.length;loop++)
            {
                System.out.println(parts[loop]);
                if (parts[loop].contains("name>"))
                {
                    first=parts[loop].substring(13, (parts[loop].length())-16);
                }
                if (parts[loop].contains("jobTitle>"))
                {
                    second=parts[loop].substring(9, (parts[loop].length())-7);
                }
                if (parts[loop].contains("worksFor>"))
                {
                    third=parts[loop].substring(9, (parts[loop].length())-7);
                }
            }
        }

        in.close();
        System.out.println("Extracted first name "+first);
        System.out.println("Extracted surname "+second);
        try {
            /////////////////////////////
            //Creating an empty XML Document




            //Creating the XML tree

            Element root = doc.createElement("address_1");
            doc.appendChild(root);



            //create child element, add an attribute, and add to root
            Element first1 = doc.createElement("Name");
            root.appendChild(first1);

            //add a text element to the child
            Text text = doc.createTextNode(first);
            first1.appendChild(text);

            Element first2 = doc.createElement("Job_title");
            root.appendChild(first2);

            //add a text element to the child
            Text text2 = doc.createTextNode(second);
            first2.appendChild(text2);
            //Output the XML

            Element first3 = doc.createElement("Employer");
            root.appendChild(first3);

            //add a text element to the child
            Text text3 = doc.createTextNode(third);
            first3.appendChild(text3);
            //Output the XML

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //print xml
            System.out.println("Here's the xml:\n\n" + xmlString);

        } catch (Exception e) {
            System.out.println(e);

        }
        saveXMLDocument("e://example2.xml", doc);
    }
    public static boolean saveXMLDocument(String fileName, Document doc) {
        System.out.println("Saving XML file... " + fileName);
        // open output stream where XML Document will be saved
        File xmlOutputFile = new File(fileName);
        FileOutputStream fos;
        Transformer transformer;
        try {
            fos = new FileOutputStream(xmlOutputFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("Error occured: " + e.getMessage());
            return false;
        }
        // Use a Transformer for output
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException e) {
            System.out.println("Transformer configuration error: " + e.getMessage());
            return false;
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fos);
        // transform source into result will do save
        try {
            transformer.transform(source, result);
        }
        catch (TransformerException e) {
            System.out.println("Error transform: " + e.getMessage());
        }
        System.out.println("XML file saved.");
        return true;
    }
}
