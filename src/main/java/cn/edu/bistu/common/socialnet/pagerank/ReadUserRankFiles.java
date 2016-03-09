package cn.edu.bistu.common.socialnet.pagerank;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by tanjie on 11/15/15.
 */
public class ReadUserRankFiles {
    protected static Logger log = Logger.getLogger(ReadUserRankFiles.class);

    private static String filePath = "/Users/tanjie/Documents/Master Files/Corpus/Data";
    public static HashMap<String, String> map = new HashMap<>();
    /**
     * 读取文件数据
     * @param num
     * @return
     */
    public static UserGraph readFiles(int num, int pernum) throws IOException {
        UserGraph userGraph = new UserGraph(num);

        List<String> list = new ArrayList<>();
        List<String> all = new ArrayList<>();
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<String> queueName = new LinkedList<>();


        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for(File f: files) {
            all.add(f.getName());
        }

        list.add("1197161814");

        String seed = filePath + "/1197161814";

        int c = 1;

        String attention = "";
        String s = "";
        while(c < num) {
            if(c==1) {
                attention = seed;
                s = "李开复";
                map.put("1197161814", "李开复");
            } else {
                attention = queue.remove();
                s = queueName.remove();
            }

            BufferedReader br = new BufferedReader(new FileReader(attention + "/Attention.txt"));

            String line = "";
            int n = 0;

            while((line = br.readLine()) != null && n<pernum && c<num) {
                String[] strs = line.split(" +");
                if(strs.length == 2) {
                    String id = strs[0];
                    String name = strs[1].substring(0, strs[1].indexOf('('));
                    if(all.contains(id)){
                        BufferedReader bra = new BufferedReader(new FileReader(filePath + "/" + id + "/weibo.txt"));
                        bra.readLine();
                        String value = bra.readLine();
                        if(value == null || value.equals(""))
                            continue;
                        String[] values = value.split(" +");
                        double cp = (Double.parseDouble(values[2]) + Double.parseDouble(values[3])) / 2.0;
                        c++;
                        if(!list.contains(id)) {
                            queue.add(filePath + "/" + id);
                            queueName.add(name);
                        }
                        list.add(id);
                        log.info(c+ " " + id);
                        userGraph.addEdge(list.indexOf(attention.substring(attention.lastIndexOf('/') + 1)), list.indexOf(id), cp);

                        map.put(id, name);
                        n++;
                    }
                }
            }
            br.close();
        }
        return userGraph;
    }
}
