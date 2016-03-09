package cn.edu.bistu.common.data;

import cn.edu.bistu.weibo.dao.WeibosMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Weibos;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by tanjie on 12/21/15.
 */
public class WeiboContentHandle {
    protected static Logger logger = Logger.getLogger(WeiboContentHandle.class);

    public static void main(String[] args) throws IOException {
        String dir = "/Users/tanjie/Documents/Master Files/Corpus/Data";
        File file = new File(dir);
        File[] files = file.listFiles();

        SqlSession session = SessionFactory.getFactory().openSession();
        WeibosMapper weibosMapper = session.getMapper(WeibosMapper.class);

        for(File f: files) {
            if (f.isDirectory()) {
                String weiboInfoPath = f.getPath() + "/weiboInfo.txt";
                logger.info(weiboInfoPath);
                BufferedReader br = new BufferedReader(new FileReader(weiboInfoPath));
                String line = "";
                logger.info("user: " + f.getName() + "  start");
                while ((line = br.readLine()) != null){
                    if(StringUtils.isEmpty(line))
                        continue;
                    String w = EmojiFilter.filterEmoji(line);
                    if(w.lastIndexOf("[不实信息]") != -1)
                        w = w.substring(0, w.lastIndexOf("[不实信息]"));
                    int comm = Integer.parseInt(w.substring(w.lastIndexOf("评论[") + 3, w.lastIndexOf("]")));
                    //logger.info(comm);
                    w = w.substring(0, w.lastIndexOf("评论["));
                    int trans = Integer.parseInt(w.substring(w.lastIndexOf("转发[") + 3, w.lastIndexOf("]")));
                    //logger.info(trans);
                    w = w.substring(0, w.lastIndexOf("转发["));
                    int zan = Integer.parseInt(w.substring(w.lastIndexOf("赞[") + 2, w.lastIndexOf("]")));
                    //logger.info(zan);
                    w = w.substring(0, w.lastIndexOf("赞[")).trim();
                    //logger.info(w);
                    if(w.indexOf("     ") == -1)
                        continue;
                    w = w.substring(w.indexOf("     ") + 5).trim();
                    //logger.info(f.getName() + " " + w);
                    Weibos weibos = new Weibos(f.getName(), w, zan, trans, comm);
                    weibosMapper.insert(weibos);
                }
                session.commit();
                logger.info("user: " + f.getName() + "  finish");
            }
        }
        session.close();
    }
}
