package cn.edu.bistu.common.nlp.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.bistu.weibo.dao.SentimentTestMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.SentimentTest;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.*;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by tanjie on 10/25/15.
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        String urlString = "http://ltpapi.voicecloud.cn/analysis/?api_key=P488s6Y3SRiayGZmgVuhtxEwjMLvouDHNYBGEV0v&text=我不高兴&pattern=dp&format=json";
//        URLEncoder.encode(urlString, "UTF-8");
//        System.out.println(urlString);
//        URL url = new URL(urlString);
//        URLConnection connection = url.openConnection();
//        connection.connect();
//        InputStream inputStream = connection.getInputStream();
//
//        int size = inputStream.available();
//        byte[] bytes = new byte[size];
//        inputStream.read(bytes);
//        String content = new String(bytes, "UTF-8");
//        System.out.println(content);

//        List<Term> parse0 = ToAnalysis.parse("我不高兴");
//        List<Term> parse = ToAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。");
//        List<Term> parse2 = ToAnalysis.parse("确实。但不管那些事有没有影响你的生存质量。你应该有自己的一套价值观。表达你的观点寻求更完善的体系。//@宁财神: 要以大局为重嘛！");
//
//        System.out.println(parse.get(3).getName());
//        System.out.println(parse.get(3).getNatureStr());
//        System.out.println(parse.get(3).getOffe());
//        System.out.println(parse.get(3).getRealName());
//        System.out.println(parse0);
//        System.out.println(parse);
//        System.out.println(parse2);



//        Pattern pattern = Pattern.compile("\\[[a-z0-9\\u4e00-\\u9fa5]+\\]");
//        String s = "尼玛！！！！好好听！！！！！！！！！！！！我禁不住的鼓鼓掌[鼓掌][v5] [鼓掌] [鼓掌] [鼓掌] [鼓掌] [鼓掌] [鼓掌]";
//        //System.out.println(s.matches("\\[[\\u4e00-\\u9fa5]+\\]"));
//        Matcher m = pattern.matcher(s);
//        System.out.println(m.matches());
//        while (m.find()) {
//            String s1 = m.group();
//            System.out.println(s1.substring(1, s1.length()-1));
//        }
        SqlSession session = SessionFactory.getFactory().openSession();

        SentimentTestMapper sentimentTestMapper = session.getMapper(SentimentTestMapper.class);

        List<SentimentTest> list = sentimentTestMapper.queryCompleteAll();
        int a=0;//删除中立的
        int b=0;//删除正面
        int c=0;//删除负面
        for(SentimentTest s: list) {
            if(s.getCorrect() == 1 && s.getWeight() != 0.5f) {
                if(a<400){
                    sentimentTestMapper.deleteById(s.getId());
                    a++;
                }
            }
            a=0;
            if(s.getCorrect() == 1 && s.getWeight() == 0.5f) {
                if(a<400){
                    sentimentTestMapper.deleteById(s.getId());
                    a++;
                }
            }
            if(s.getCorrect()==2 && s.getWeight()!=1.0f){
                if(b<500){
                    sentimentTestMapper.deleteById(s.getId());
                    b++;
                }
            }
            if(s.getCorrect()==0 && s.getWeight()!=0.0f){
                if(c<400){
                    sentimentTestMapper.deleteById(s.getId());
                    c++;
                }
            }
        }
        session.commit();
        session.close();
    }
}
