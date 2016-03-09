package cn.edu.bistu.weibo.service;

import cn.edu.bistu.weibo.dao.AttentionMapper;
import cn.edu.bistu.weibo.dao.UsersMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Attention;
import cn.edu.bistu.weibo.model.Users;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tanjie on 11/7/15.
 */
@Path("/SocialNet")
public class SocialNetRest {
    protected static Logger log = Logger.getLogger(SocialNetRest.class);

    @POST
    @Path("example")
    @Produces("application/json;charset=UTF-8")
    public JSONArray getExampleData() throws IOException {
        String path = "/Users/tanjie/Documents/Master Files/Corpus/Data";

        JSONArray json = new JSONArray();
        List<String> list = new ArrayList<>();
        List<String> all = new ArrayList<>();
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<String> queueName = new LinkedList<>();
        list.add("1197161814");

        File dir = new File(path);
        File[] files = dir.listFiles();
        for(File f: files) {
            all.add(f.getName());
        }
        log.info(all.size());

        String seed = path + "/1197161814";

        int c = 0;

        String attention = "";
        String s = "";
        while(c < 100) {
            if(c==0) {
                attention = seed;
                s = "李开复";
            } else {
                attention = queue.remove();
                s = queueName.remove();
            }

            BufferedReader br = new BufferedReader(new FileReader(attention + "/Attention.txt"));

            String line = "";
            int n = 0;

            while((line = br.readLine()) != null && n<6) {
                String[] strs = line.split("    ");
                if(strs.length == 2) {
                    String id = strs[0];
                    String name = strs[1].substring(0, strs[1].indexOf('('));
//                    if(list.contains(id))
//                        continue;
                    if(all.contains(id)){
                        json.add(new JSONObject().element("source", s).element("target", name));
                        c++;
                        log.info(name);
                        if(!list.contains(id)) {
                            queue.add(path + "/" + id);
                            queueName.add(name);
                        }
                        list.add(id);
                        n++;
                    }
                }
            }
            log.info(n);
            br.close();
        }

        return json;
    }

    @GET
    @Path("/usernet")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json;charset=UTF-8")
    public JSONArray getNetData(@QueryParam("id") String id){
        SqlSession session = SessionFactory.getFactory().openSession();
        AttentionMapper attentionMapper = session.getMapper(AttentionMapper.class);
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        JSONArray json = new JSONArray();
        List<String> list = new ArrayList<>();
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<String> queueName = new LinkedList<>();
        if(StringUtils.isEmpty(id)){
            id = "1197161814";
        }
        list.add(id);

        int c = 0;

        String attention = "";
        String s = "";
        while(c < 150) {
            if(c==0) {
                attention = id;
                s = usersMapper.queryUserById(attention).getName();
            } else {
                attention = queue.remove();
                s = queueName.remove();
            }

            List<Attention> l = attentionMapper.queryByRandom(10, attention);

            int n = 0;

            for(Attention a: l){
                Users u = usersMapper.queryUserById(a.getAttentionUserid());
                json.add(new JSONObject().element("source", s).element("target", u.getName()));
                c++;
                if(!list.contains(u.getId())) {
                    queue.add(u.getId());
                    queueName.add(u.getName());
                    list.add(u.getId());
                }
                n++;
            }
            if(queue.size() == 0)
                break;
        }

        session.close();
        return json;
    }

    public static void main(String[] args) throws IOException {
        log.info(new SocialNetRest().getExampleData().toString());
    }
}
