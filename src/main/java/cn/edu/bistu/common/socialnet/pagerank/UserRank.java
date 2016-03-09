package cn.edu.bistu.common.socialnet.pagerank;

import cn.edu.bistu.weibo.dao.AttentionMapper;
import cn.edu.bistu.weibo.dao.RankMapper;
import cn.edu.bistu.weibo.dao.UsersMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Attention;
import cn.edu.bistu.weibo.model.Rank;
import cn.edu.bistu.weibo.model.Users;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tanjie on 11/15/15.
 */
public class UserRank {
    protected static Logger logger = Logger.getLogger(UserRank.class);

    public static void calRank(){
        SqlSession session = SessionFactory.getFactory().openSession();
        RankMapper rankMapper = session.getMapper(RankMapper.class);
        AttentionMapper attentionMapper = session.getMapper(AttentionMapper.class);
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        List<String> userids = new ArrayList<>();
        List<Users> userses = usersMapper.queryUsers();
        for(Users s: userses){
            userids.add(s.getId());
        }
        List<Attention> attentions = attentionMapper.queryAll();

        UserGraph userGraph = new UserGraph(userids.size());
        //Graph graph = new Graph(userids.size());
        for(Attention a: attentions){
            Users u = userses.get(userids.indexOf(a.getUserid()));
            double cp = ((double)u.getTrans() + (double)u.getComm() + 1) / 2.0;
            userGraph.addEdge(userids.indexOf(a.getUserid()), userids.indexOf(a.getAttentionUserid()), cp);
            //graph.addEdge(userids.indexOf(a.getUserid()), userids.indexOf(a.getAttentionUserid()));
        }

        Vector vector = userGraph.computePagerank(0.85, 0.0001);
        //Vector vector = graph.computePagerank(0.85, 0.0001);
        //logger.info(vector);
        double max = 0;
        double min = 100000;
        logger.info(vector.size());
        for(int i=0;i<vector.size();i++){
            double d = vector.get(i);
            if(d>max)
                max=d;
            if(d<min)
                min=d;
        }
        logger.info("max: "+max);
        logger.info("min: "+min);
        double minus = max - min;

        for(int i=0; i<vector.size();i++){
            String userid = userids.get(i);
            double rank = vector.get(i);
            double v = (rank - min)/minus;

            rankMapper.updateRank(userid, rank, v);
        }
        session.commit();
        session.close();
    }

    public static void main(String[] args) throws IOException {
        //double d=1.0/15992;
        //logger.info(d);
        calRank();
//        String s = "/12/312/4131";
//        System.out.println(s.substring(s.lastIndexOf('/')+1));
//        String str = "1197151844       13394       10369        2832         715         5398806     389         40";
//        String[] strs = str.split(" +");
//        System.out.println(strs.length);
//        for(String x:strs){
//            System.out.println(x);
//        }
//        UserGraph userGraph = ReadUserRankFiles.readFiles(10, 5);
//
//        Vector vector = userGraph.computePagerank(0.85, 0.0001);
//
//        System.out.println(vector);
//
//        for(Map.Entry<String, String> es:ReadUserRankFiles.map.entrySet()) {
//            System.out.println(es.getKey() + "   " + es.getValue());
//        }
    }
}
