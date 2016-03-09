package cn.edu.bistu.common.weibocredit;

import cn.edu.bistu.common.nlp.libsvm.svm.svm_node;
import cn.edu.bistu.common.nlp.sentiment.analysis.bayes.Analysis;
import cn.edu.bistu.weibo.dao.WeibosMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Weibos;
import net.sf.json.JSONObject;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanjie on 12/22/15.
 */
public class WeiboCreditCalculate {
    protected static Logger logger = Logger.getLogger(WeiboCreditCalculate.class);

    private static String dirPath = WeiboCreditCalculate.class.getResource("/sentiment/sentiment_dic/").getPath();
    private static String f = WeiboCreditCalculate.class.getResource("/credibility/data/train.txt").getPath();
    //情感词典
    private static HashSet<String> emos = null;

    private String id;
    private String weibo;

//    private int em=0;//倾向
//    private int http=0;//外链
//    private int at=0;//at数
//    private int topic=0;//话题
//    private int hasemo=0;//是否有情感词
//    private int emotion=0;//情感词数
//    private int emo=0;//表情数
//    private int sjh=0;//书籍
//    private int none=0;//名词

    public WeiboCreditCalculate(String id, String weibo) throws IOException {
        this.id = id;
        this.weibo = weibo;
        if(emos == null){
            init();
        }
    }

    public void init() throws IOException {
        emos = new HashSet<>();
        String posDic = dirPath + "ntusd-positive.txt";
        String negDic = dirPath + "ntusd-negative.txt";
        String line = "";
        BufferedReader posBr = new BufferedReader(new FileReader(posDic));
        while((line = posBr.readLine()) != null) {
            emos.add(line);
        }
        posBr.close();
        BufferedReader negBr = new BufferedReader(new FileReader(negDic));
        while((line = negBr.readLine()) != null) {
            emos.add(line);
        }
        negBr.close();
    }

    public int calem() throws IOException {
        Analysis analysis = new Analysis(0);
        int e = analysis.classfyWithNeg(weibo);
        if(e == 2){
            e=1;
        }
        return e;
    }

    public int calUrl() {
        int x=0;
        String w = weibo;
        while(w.contains("http:")){
            x++;
            w=w.substring(w.indexOf("http:")+5);
        }
        return x;
    }

    public int calAt(){
        int x=0;
        String w = weibo;
        while(w.contains("@")){
            x++;
            w=w.substring(w.indexOf("@")+1);
        }
        return x;
    }

    public int calTopic(){
        int x=0;
        String w = weibo;
        while(w.contains("#")){
            x++;
            w=w.substring(w.indexOf("#")+1);
        }
        x=x/2;
        w = weibo;
        while(w.contains("【")){
            x++;
            w=w.substring(w.indexOf("【")+1);
        }
        return x;
    }

    public int calEmoWords(){
        int x=0;
        List<Term> l = BaseAnalysis.parse(weibo);
        for(Term t:l){
            if(emos.contains(t.getName()))
                x++;
        }
        return x;
    }

    public int calEmo(){
        Pattern pattern = Pattern.compile("\\[[a-z0-9\\u4e00-\\u9fa5]+\\]");
        Matcher m = pattern.matcher(weibo);
        int n=0;
        while(m.find())
            n++;
        return n;
    }

    public int calBook(){
        int x=0;
        String w = weibo;
        while(w.contains("《")){
            x++;
            w=w.substring(w.indexOf("《")+1);
        }
        return x;
    }

    public int calNoun(){
        int x=0;
        List<Term> l = BaseAnalysis.parse(weibo);
        for(Term t:l){
            if(t.getNatureStr().equals("n"))
                x++;
        }
        return x;
    }

    public JSONObject extractInfo() throws IOException {
        int emword = calEmoWords();
        JSONObject json = new JSONObject()
                .element("length", weibo.length())
                .element("em", calem())
                .element("url", calUrl())
                .element("at", calAt())
                .element("tp", calTopic())
                .element("hasemw", emword==0?0:1)
                .element("emwords", emword)
                .element("emo", calEmo())
                .element("book", calBook())
                .element("noun", calNoun());
        return json;
    }

    public svm_node[] parseNodes() throws IOException {
        int emword = calEmoWords();
        svm_node[] nodes = new svm_node[9];
        for(int i=0;i<9;i++){
            nodes[i]=new svm_node();
        }
        nodes[0].index = 1;
        nodes[0].value = calem();
        nodes[1].index = 2;
        nodes[1].value = calUrl();
        nodes[2].index = 3;
        nodes[2].value = calAt();
        nodes[3].index = 4;
        nodes[3].value = calTopic();
        nodes[4].index = 5;
        nodes[4].value = emword==0?0:1;
        nodes[5].index = 6;
        nodes[5].value = emword;
        nodes[6].index = 7;
        nodes[6].value = calEmo();
        nodes[7].index = 8;
        nodes[7].value = calBook();
        nodes[8].index = 9;
        nodes[8].value = calNoun();
        return nodes;
    }

    public static void main(String[] args) throws IOException {
//        List<Term> l = BaseAnalysis.parse("桌子很快乐");
//        logger.info(l);
//        for(Term t:l){
//            logger.info(t.getName());
//            logger.info(t.getNatureStr());
//        }
//        WeiboCreditCalculate weiboCreditCalculate = new WeiboCreditCalculate("123", "就发了受打击了副科级阿里可视对讲伐啦");
//        int r = PredictWeibo.predict("123", "就发了受打击了副科级阿里可视对讲伐啦");
//        JSONObject json = new JSONObject();
//        json.element("seg", BaseAnalysis.parse("就发了受打击了副科级阿里可视对讲伐啦")).element("info", weiboCreditCalculate.extractInfo());
//        json.element("rumour", r);
//        json.element("credit", PredictWeibo.calCredit(r, 0.8767));
//        logger.info(json);

        SqlSession session = SessionFactory.getFactory().openSession();
        WeibosMapper weibosMapper = session.getMapper(WeibosMapper.class);

        //List<Weibos> l = weibosMapper.queryByUserId("1663148275");
        List<String> l = new ArrayList<>();
        //String f = WeiboCreditCalculate.class.getResource("/credibility/data/tran.txt").getPath();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = "";
        while((line=br.readLine())!=null){
            l.add(line);
        }
        session.close();
        List<String> ws = new ArrayList<>();
        for(String w:l){
            try{
                if(PredictWeibo.predict("", w) == 1 )
                    ws.add(w);
            }catch (Exception e){

            }
        }

        logger.info("count:"+ws.size());
        for(String w: ws){
            logger.info("ww++" + w.toString());
        }
    }
}
