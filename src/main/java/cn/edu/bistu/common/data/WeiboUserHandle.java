package cn.edu.bistu.common.data;

import cn.edu.bistu.weibo.dao.UsersMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Users;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by tanjie on 12/18/15.
 */
public class WeiboUserHandle {
    protected static Logger logger = Logger.getLogger(WeiboUserHandle.class);

    public static void main(String[] args) throws IOException {
        String dir = "/Users/tanjie/Documents/Master Files/Corpus/Data";
        File file = new File(dir);
        File[] files = file.listFiles();

        SqlSession sqlSession = SessionFactory.getFactory().openSession();
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);

        for(File f: files) {
            if(f.isDirectory()){
                if(usersMapper.queryUserById(f.getName()) != null)
                    continue;
                Users users = new Users();
                String userInfoPath = f.getPath() + "/userInfo.txt";
                BufferedReader bufferedReader = new BufferedReader(new FileReader(userInfoPath));
                String info = bufferedReader.readLine();
                bufferedReader.close();

                if(info.indexOf("标签:") != -1) {
                    String tag = info.substring(info.indexOf("标签:") + 3);
                    tag = transTag(tag);
                    users.setTag(tag);
                    info = info.substring(0, info.indexOf("标签:")).trim();
                }
                if(info.indexOf("简介:") != -1) {
                    String summary = info.substring(info.indexOf("简介:") + 3);
                    summary = EmojiFilter.filterEmoji(summary);
                    users.setSummary(summary.trim());
                    info = info.substring(0, info.indexOf("简介:")).trim();
                }
                if(info.indexOf("性取向：") != -1) {
                    info = info.substring(0, info.indexOf("性取向：")).trim();
                }
                if(info.indexOf("认证信息：") != -1){
                    info = info.substring(0, info.indexOf("认证信息：")).trim();
                }
                if(info.indexOf("生日:") != -1){
                    String birthday = info.substring(info.indexOf("生日:") + 3);
                    if(!birthday.equals("0001-00-00")){
                        users.setBirthday(birthday);
                    }
                    info = info.substring(0, info.indexOf("生日:"));
                }
                if(info.indexOf("地区:") != -1){
                    String area = info.substring(info.indexOf("地区:") + 3);
                    users.setArea(area.trim());
                    info = info.substring(0, info.indexOf("地区:")).trim();
                }
                //默认性别：男
                users.setSex("男");
                if(info.indexOf("性别:") != -1){
                    String sex = info.substring(info.indexOf("性别:") + 3);
                    if(sex.length() == 1) {
                        users.setSex(sex);
                    }
                    info = info.substring(0, info.indexOf("性别:")).trim();
                }
                if(info.indexOf("达人:") != -1) {
                    info = info.substring(0, info.indexOf("达人:")).trim();
                }
                if(info.indexOf("认证:") != -1){
                    String official = info.substring(info.indexOf("认证:") + 3);
                    users.setOfficial(official);
                    info = info.substring(0, info.indexOf("认证:")).trim();
                }
                users.setName(f.getName());
                if(info.indexOf("昵称:") != -1){
                    String name = info.substring(info.indexOf("昵称:") + 3);
                    name = EmojiFilter.filterEmoji(name);
                    users.setName(name);
                }
                users.setId(f.getName());


                String weiboPath = f.getPath() + "/weibo.txt";

                bufferedReader = new BufferedReader(new FileReader(weiboPath));

                String tagLine = bufferedReader.readLine();
                String valueLine = bufferedReader.readLine();

                bufferedReader.close();

                if(StringUtils.isBlank(tagLine) || StringUtils.isBlank(valueLine)){
                    continue;
                }

                String[] tags = tagLine.split(" +");
                String[] values = valueLine.split(" +");

                if(tags.length != values.length){
                    continue;
                }

                for(int i=0; i<tags.length; i++){
                    switch (tags[i]){
                        case "获赞数":
                            users.setZan(Integer.parseInt(values[i]));
                            break;
                        case "获转发数":
                            users.setTrans(Integer.parseInt(values[i]));
                            break;
                        case "获评论数":
                            users.setComm(Integer.parseInt(values[i]));
                            break;
                        case "关注数":
                            users.setAtt(Integer.parseInt(values[i]));
                            break;
                        case "粉丝数":
                            users.setFans(Integer.parseInt(values[i]));
                            break;
                        case "微博数":
                            users.setWeibos(Integer.parseInt(values[i]));
                            break;
                        case "总页数":
                            users.setPages(Integer.parseInt(values[i]));
                            break;
                    }
                }

                logger.info(users.toString());
                usersMapper.insert(users);
                sqlSession.commit();
            }
        }
        sqlSession.close();
    }

    private static String transTag(String tag) {
        String[] words = tag.split(" +");
        String newTag = "";
        //logger.info(words.length);
        for(String s: words) {
            //logger.info(s);
            if(s.equals("") || s.equals(null))
                continue;
            newTag = newTag + " " + s;
        }
        return newTag.substring(1);
    }
}
