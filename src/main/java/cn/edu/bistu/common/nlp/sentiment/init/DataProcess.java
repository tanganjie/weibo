package cn.edu.bistu.common.nlp.sentiment.init;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;

/**
 * Created by tanjie on 10/15/15.
 */
public class DataProcess {
    protected static Logger log = Logger.getLogger(DataProcess.class);

    private static String linkline(Element element) {
        StringBuilder sb = new StringBuilder();
        for(Object o: element.elements()) {
            Element e = (Element) o;
            sb.append(e.getText());
        }
        return sb.toString();
    }

    /**
     * sentiment train data parse
     * @throws DocumentException
     * @throws IOException
     */
    public static void dataProcess() throws DocumentException, IOException {
        String path = DataProcess.class.getResource("/sentiment/train.xml").getPath();

        log.info(path);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File(path));
        log.info("Load success...");
        Element root = doc.getRootElement();
        log.info("size: " + root.elements().size());

        String newpath = "/Users/tanjie/Train.txt";
        log.info(newpath);
        File newfile = new File(newpath);
        if(newfile.exists())
            newfile.delete();
        newfile.createNewFile();
        FileWriter fw = new FileWriter(newfile);

        for(Object o: root.elements()) {
            Element e = (Element) o;
            String id = e.attribute("id").getValue();
            String emotion = e.attribute("emotion-type1").getValue();
            //log.info(emotion);
            //break;
            StringBuilder sb;
            switch (emotion) {
                case "disgust":
                case "fear":
                case "sadness":
                case "anger":
                    sb = new StringBuilder();
                    sb.append(id);
                    sb.append("\t");
                    sb.append(0);
                    sb.append("\t");
                    sb.append(linkline(e));
                    sb.append("\r\n");
                    fw.write(sb.toString());
                    break;
                case "happiness":
                case "like":
                case "surprise":
                    sb = new StringBuilder();
                    sb.append(id);
                    sb.append("\t");
                    sb.append(1);
                    sb.append("\t");
                    sb.append(linkline(e));
                    sb.append("\r\n");
                    fw.write(sb.toString());
                    break;
                default:
                    sb = new StringBuilder();
                    sb.append(id);
                    sb.append("\t");
                    sb.append(0.5);
                    sb.append("\t");
                    sb.append(linkline(e));
                    sb.append("\r\n");
                    fw.write(sb.toString());
                    break;
            }
        }
        fw.flush();
        fw.close();
        log.info("parse success...");
    }

    /**
     * sentiment test data parse
     * @throws DocumentException
     * @throws IOException
     */
    public static void testDataProcess() throws DocumentException, IOException {
        String path = DataProcess.class.getResource("/sentiment/test.xml").getPath();

        log.info(path);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File(path));
        log.info("Load success...");
        Element root = doc.getRootElement();
        log.info("size: " + root.elements().size());

        String newpath = "/Users/tanjie/test.txt";
        log.info(newpath);
        File newfile = new File(newpath);
        if(newfile.exists())
            newfile.delete();
        newfile.createNewFile();
        FileWriter fw = new FileWriter(newfile);

        for(Object o: root.elements()) {
            Element e = (Element) o;
            String id = e.attribute("id").getValue();
            //log.info(emotion);
            //break;
            StringBuilder sb = new StringBuilder();

            sb.append(id);
            sb.append("\t");
            sb.append(linkline(e));
            sb.append("\r\n");
            fw.write(sb.toString());
        }
        fw.flush();
        fw.close();
        log.info("parse success...");
    }

    /**
     * answer data parse
     * @throws DocumentException
     * @throws IOException
     */
    public static void answerDataProcess() throws DocumentException, IOException {
        String path = DataProcess.class.getResource("/sentiment/EmotionClassficationTest.xml").getPath();
        String answertxt = DataProcess.class.getResource("/sentiment/EmotionClassificationID.txt").getPath();

        log.info(path);
        log.info(answertxt);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File(path));
        log.info("Load success...");
        Element root = doc.getRootElement();
        log.info("size: " + root.elements().size());
        FileReader fr = new FileReader(answertxt);
        BufferedReader br = new BufferedReader(fr);

        String newpath = "/Users/tanjie/answer.txt";
        log.info(newpath);
        File newfile = new File(newpath);
        if(newfile.exists())
            newfile.delete();
        newfile.createNewFile();
        FileWriter fw = new FileWriter(newfile);

        for(Object o: root.elements()) {
            Element e = (Element) o;
            //String id = e.attribute("id").getValue();
            String id = br.readLine().trim();
            String emotion = e.attribute("emotion-type1").getValue();
            //log.info(emotion);
            //break;
            StringBuilder sb = new StringBuilder();

            switch (emotion) {
                case "disgust":
                case "fear":
                case "sadness":
                case "anger":
                    sb = new StringBuilder();
                    sb.append(id);
                    sb.append("\t");
                    sb.append(0);
                    sb.append("\r\n");
                    fw.write(sb.toString());
                    break;
                case "happiness":
                case "like":
                case "surprise":
                    sb = new StringBuilder();
                    sb.append(id);
                    sb.append("\t");
                    sb.append(1);
                    sb.append("\r\n");
                    fw.write(sb.toString());
                    break;
                default:
                    sb = new StringBuilder();
                    sb.append(id);
                    sb.append("\t");
                    sb.append(0.5);
                    sb.append("\r\n");
                    fw.write(sb.toString());
                    break;
            }
        }
        fw.flush();
        fw.close();
        log.info("parse success...");
    }

    public static void main(String[] args) throws Exception {
        //dataProcess();
        //testDataProcess();
        answerDataProcess();
    }
}
