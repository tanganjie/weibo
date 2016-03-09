package cn.edu.bistu.common.nlp.test;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * Created by tanjie on 11/6/15.
 */
public class UserAnalysisTest {
    protected static Logger log = Logger.getLogger(UserAnalysisTest.class);

    public static void main(String[] args) throws DocumentException {
        String path = "/Users/tanjie/Documents/Master Files/Corpus/";
        File file1 = new File(path + "my");
        File file2 = new File(path + "user");

        int n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0;

        File[] files = file1.listFiles();
        File[] files2 = file2.listFiles();

        SAXReader reader = new SAXReader();

        for(File f: files) {
            if(f.getName().indexOf("xml") == -1)
                continue;
            Document doc = reader.read(f);
            Element root = doc.getRootElement();
            Element node = root.element("jiaohu");
            int x = Integer.parseInt(node.element("粉丝").getText());
            if(x<=100)
                n1++;
            else if(x<=10000)
                n2++;
            else if(x<=1000000)
                n3++;
            else if(x<=10000000)
                n4++;
            else
                n5++;
        }

        for(File f: files2) {
            if(f.getName().indexOf("xml") == -1)
                continue;
            Document doc = reader.read(f);
            Element root = doc.getRootElement();
            Element node = root.element("jiaohu");
            int x = Integer.parseInt(node.element("粉丝").getText());
            if(x<=100)
                n1++;
            else if(x<=10000)
                n2++;
            else if(x<=1000000)
                n3++;
            else if(x<=10000000)
                n4++;
            else
                n5++;
        }

        log.info(n1);
        log.info(n2);
        log.info(n3);
        log.info(n4);
        log.info(n5);
    }
}
