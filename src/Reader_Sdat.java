import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;

class Reader_Sdat {
    private ArrayList<Sdat> output = new ArrayList<>();

    private void readFile(File f) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            Sdat sdat = new Sdat();
            //Get Document ID
            NodeList idlist = doc.getElementsByTagName("rsm:DocumentID");
            Node id = idlist.item(0);
            Element idelement = (Element) id;
            sdat.setDocumentID(idelement.getTextContent());
            //Get Start- Enddate
            NodeList intervallist = doc.getElementsByTagName("rsm:Interval");
            Node interval = intervallist.item(0);
            Element intervalelement = (Element) interval;
            Element start = (Element) (intervalelement).getElementsByTagName("rsm:StartDateTime").item(0);
            Element end = (Element) (intervalelement).getElementsByTagName("rsm:EndDateTime").item(0);
            sdat.setStartDateTime(start.getTextContent());
            sdat.setEndDateTime(end.getTextContent());
            // Get Resolution value + unit
            Resolution resolution = new Resolution();
            NodeList resolist = doc.getElementsByTagName("rsm:Resolution");
            Node resoNode = resolist.item(0);
            Element resoElement = (Element) resoNode;
            String resoString = resoElement.getTextContent();
            String numbers = resoString.replaceAll("\\D+","");
            resolution.setValue(Integer.parseInt(numbers));
            String unit = resoString.replaceAll("[^A-Za-z]+","");
            resolution.setUnit(unit);
            sdat.setResolution(resolution);
            // Get Observation

            ArrayList<Observation> list = new ArrayList<>();
            NodeList oblist = doc.getElementsByTagName("rsm:Observation");
            for (int i = 0; i < oblist.getLength(); i++){
                Observation observation = new Observation();
                Node nodeob = oblist.item(i);
                NodeList childlist = nodeob.getChildNodes();
                for (int j = 0; i < 2; i++){
                    Node childnode = childlist.item(j);
                    if (j == 0){
                        Node sequence = childlist.item(j);
                        System.out.println(sequence.getTextContent());
                    } else {

                    }

                }
                //observation.setSequence(Integer.parseInt());

            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }




    void readAllFiles() {
        try {
            String[] fileNames =
                    Files.list(Paths.get(".\\bin\\SDAT-Files")).filter(
                            Files::isRegularFile).map(
                            p -> p.toFile().getName()).toArray(String[]::new);
            for (String s : fileNames) {
                File filepath = new File(".\\bin\\SDAT-Files\\" + s);
                readFile(filepath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
