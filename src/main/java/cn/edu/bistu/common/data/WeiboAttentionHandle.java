package cn.edu.bistu.common.data;

import cn.edu.bistu.weibo.dao.AttentionMapper;
import cn.edu.bistu.weibo.dao.UsersMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Attention;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by tanjie on 12/20/15.
 */
public class WeiboAttentionHandle {
    protected static Logger logger = Logger.getLogger(WeiboAttentionHandle.class);

    public static void main(String[] args) throws IOException {
        String dir = "/Users/tanjie/Documents/Master Files/Corpus/Data";
        File file = new File(dir);
        File[] files = file.listFiles();

        SqlSession session = SessionFactory.getFactory().openSession();
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        AttentionMapper attentionMapper = session.getMapper(AttentionMapper.class);

        for(File f: files) {
            if(f.isDirectory()){
                if(usersMapper.queryUserById(f.getName()) == null)
                    continue;
                String path = f.getPath() + "/Attention.txt";
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line = "";
                while((line = br.readLine()) != null){
                    if(StringUtils.isEmpty(line.trim()))
                        continue;
                    String[] strs = line.split(" +");
                    if(usersMapper.queryUserById(strs[0].trim()) == null)
                        continue;
                    Attention attention = new Attention(f.getName(), strs[0].trim());
                    attentionMapper.insert(attention);
                    session.commit();
                }
            }
        }
        session.close();
    }
}
