/**
 * Created by scottjohnturner on 17/11/2016.
 */
//Taken from various sources, including: http://labe.felk.cvut.cz/~xfaigl/mep/xml/java-xml.htm

import java.net.*;
import java.util.ArrayList;
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

public class urlreader {
    private static ArrayList<String> first=new ArrayList<String>();
    private static ArrayList<String> job=new ArrayList<String>();
    private static ArrayList<String> email=new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        URL[] item=new URL[2];
        item[0] = new URL("http://www.computing.northampton.ac.uk/~scott/csy3025/sample1.html");
        item[1] = new URL("http://www.computing.northampton.ac.uk/~scott/csy3025/sample2.html");
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        //BufferedReader in = new BufferedReader(
        //			new InputStreamReader(
        //			item.openStream()));

        //String inputLine;

        for(int loop1=0;loop1<2;loop1++){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            item[loop1].openStream()));
            String inputLine;
            int size1 = 0,size2=0;
            while ((inputLine = in.readLine()) != null)//reads in the html page line by line
            {
                String inputLine11=inputLine.replaceAll("\"", "");//removes speechmarks from text
                String inputLine21=inputLine11.replaceAll("<strong>", "");
                String inputLine2=inputLine21.replaceAll("</strong>", "");
                String[] parts = inputLine2.split("<span itemprop=");//splits into sections based on it is after <span class="
                for (int loop=0;loop<parts.length;loop++)
                {
                    if (parts[loop].contains("name>"))
                    {
                        first.add(parts[loop].substring(5, (parts[loop].length())-7));
                    }
                    size1=first.size();
                    if (parts[loop].contains("jobTitle>"))
                    {
                        job.add(parts[loop].substring(9, (parts[loop].length())-7));

                    }
                    if (parts[loop].contains("email>"))
                    {
                        email.add(parts[loop].substring(6, (parts[loop].length())-7));

                    }
                    size2=email.size();

                }


            }in.close();
            if (size2<size1)
            {
                email.add(null);
            }
        }

        System.out.println("Extracted first name "+first);
        System.out.println("Extracted surname "+job);
        System.out.println("Extracted surname "+email);
        try {
            /////////////////////////////
            //Creating an empty XML Document




            //Creating the XML tree

            Element root = doc.createElement("hcards");
            doc.appendChild(root);


            for (int loopx=0;loopx<2;loopx++)
            {
                Element hcard=doc.createElement("hcard");
                root.appendChild(hcard);

                Element first1 = doc.createElement("name");
                hcard.appendChild(first1);

                //add a text element to the child
                Text text = doc.createTextNode(first.get(loopx));
                first1.appendChild(text);

                Element first2 = doc.createElement("job_title");
                hcard.appendChild(first2);
                //add a text element to the child
                Text text2 = doc.createTextNode(job.get(loopx));
                first2.appendChild(text2);

                if(email.get(loopx)!=null)
                {
                    Element first3 = doc.createElement("email");
                    hcard.appendChild(first3);
                    //add a text element to the child
                    Text text3 = doc.createTextNode(email.get(loopx));
                    first3.appendChild(text3);
                }
                //Output the XML
            }
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
        saveXMLDocument("example2.xml", doc);
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
