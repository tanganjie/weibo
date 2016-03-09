package cn.edu.bistu.common.nlp.sentiment.analysis.bayes;

import cn.edu.bistu.common.nlp.util.Segment;
import cn.edu.bistu.common.nlp.util.WordSeg;
import cn.edu.bistu.weibo.dao.SentimentTestMapper;
import cn.edu.bistu.weibo.dao.SentimentTrainMapper;
import cn.edu.bistu.weibo.dao.SentimentTrainResultMapper;
import cn.edu.bistu.weibo.dao.SentimentTrainResultPlusMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Sentiment;
import cn.edu.bistu.weibo.model.TrainResult;
import cn.edu.bistu.weibo.model.TrainResultPlus;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by tanjie on 10/19/15.
 */
public class Analysis {
    protected static Logger log = Logger.getLogger(Analysis.class);

    //分词器
    private Segment seg = WordSeg.getInstance();
    //情感词典
    private static HashSet<String> neg, pos;
    //否定词表
    private static HashSet<String> negWord;
    //模型
    private static HashMap<String, Integer> negWeight, posWeight, unsWeight;

    private static int negDocNum, posDocNum, unsDocNum;

    private String testFile;

    /**
     * 初始化
     * @throws IOException
     */
    public Analysis() throws IOException {
        String dicPath = Analysis.class.getResource("/sentiment/sentiment_dic/").getPath();
        dicPath = URLDecoder.decode(dicPath, "utf-8");
        //log.info(dicPath);
        String posDic = dicPath + "ntusd-positive.txt";
        String negDic = dicPath + "ntusd-negative.txt";

        pos = new HashSet<>();
        neg = new HashSet<>();

        String line;

        //log.info("loading posDic...");
        BufferedReader posBr = new BufferedReader(new InputStreamReader(new FileInputStream(posDic), "utf-8"));
        while((line = posBr.readLine()) != null) {
            pos.add(line);
        }
        posBr.close();
        //log.info("loading posDic success...");

        //log.info("loading negDic...");
        BufferedReader negBr = new BufferedReader(new InputStreamReader(new FileInputStream(negDic), "utf-8"));
        while((line = negBr.readLine()) != null) {
            neg.add(line);
        }
        negBr.close();
        //log.info("loading negDic success...");

        testFile = Analysis.class.getResource("/sentiment/test.txt").getPath();

        //log.info("loading model...");
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainResultMapper trainResultMapper = session.getMapper(SentimentTrainResultMapper.class);
        List<TrainResult> negR = trainResultMapper.queryResultByType(0);
        List<TrainResult> unsR = trainResultMapper.queryResultByType(1);
        List<TrainResult> posR = trainResultMapper.queryResultByType(2);
        session.commit();

        negWeight = parse(negR);
        unsWeight = parse(unsR);
        posWeight = parse(posR);
        log.info("loading model success...");

        log.info("count Doc");
        SentimentTrainMapper trainMapper = session.getMapper(SentimentTrainMapper.class);
        posDocNum = trainMapper.querySizeByWeight(1f);
        negDocNum = trainMapper.querySizeByWeight(0f);
        unsDocNum = trainMapper.querySizeByWeight(0.5f);
        session.commit();
        session.close();
        log.info("count Doc success... pos:" + posDocNum + " neg:" + negDocNum + " uns:" + unsDocNum);
    }

    /**
     * 初始化、卡方检验
     * @param x2
     * @throws IOException
     */
    public Analysis(double x2) throws IOException {
        String dicPath = Analysis.class.getResource("/sentiment/sentiment_dic/").getPath();
        dicPath = URLDecoder.decode(dicPath,"utf-8");
        log.info(dicPath);
        String posDic = dicPath + "ntusd-positive.txt";
        String negDic = dicPath + "ntusd-negative.txt";
        String negWordDic = dicPath + "negWord.txt";

        pos = new HashSet<>();
        neg = new HashSet<>();
        negWord = new HashSet<>();

        String line;

        //log.info("loading posDic...");
        BufferedReader posBr = new BufferedReader(new InputStreamReader(new FileInputStream(posDic), "utf-8"));
        while((line = posBr.readLine()) != null) {
            pos.add(line);
        }
        posBr.close();
        //log.info("loading posDic success...");

        //log.info("loading negDic...");
        BufferedReader negBr = new BufferedReader(new InputStreamReader(new FileInputStream(negDic), "utf-8"));
        while((line = negBr.readLine()) != null) {
            neg.add(line);
        }
        negBr.close();
        //log.info("loading negDic success...");

        //log.info("loading negWordDic...");
        BufferedReader negWordBr = new BufferedReader(new InputStreamReader(new FileInputStream(negWordDic), "utf-8"));
        while((line = negWordBr.readLine()) != null) {
            negWord.add(line);
        }
        negWordBr.close();
        //log.info("loading negWordDic success...");

        //testFile = Analysis.class.getResource("/sentiment/test.txt").getPath();

        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainResultPlusMapper sentimentTrainResultPlusMapper = session.getMapper(SentimentTrainResultPlusMapper.class);

        List<TrainResultPlus> negR = sentimentTrainResultPlusMapper.queryAllByXType(0, x2);
        List<TrainResultPlus> unsR = sentimentTrainResultPlusMapper.queryAllByXType(1, x2);
        List<TrainResultPlus> posR = sentimentTrainResultPlusMapper.queryAllByXType(2, x2);
        session.commit();

        negWeight = parsePlus(negR);
        posWeight = parsePlus(posR);
        unsWeight = parsePlus(unsR);
        log.info("loading model success...");

        log.info("count Doc");
        SentimentTrainMapper trainMapper = session.getMapper(SentimentTrainMapper.class);
        posDocNum = trainMapper.querySizeByWeight(1f);
        negDocNum = trainMapper.querySizeByWeight(0f);
        unsDocNum = trainMapper.querySizeByWeight(0.5f);
        session.commit();
        session.close();
        log.info("count Doc success... pos:" + posDocNum + " neg:" + negDocNum + " uns:" + unsDocNum);
    }

    /**
     * 初始化、卡方检验、bool
     * @param x2
     * @param bool
     * @throws IOException
     */
    public Analysis(double x2, boolean bool) throws IOException {
        String dicPath = Analysis.class.getResource("/sentiment/sentiment_dic/").getPath();
        //log.info(dicPath);
        String posDic = dicPath + "ntusd-positive.txt";
        String negDic = dicPath + "ntusd-negative.txt";
        String negWordDic = dicPath + "negWord.txt";

        pos = new HashSet<>();
        neg = new HashSet<>();
        negWord = new HashSet<>();

        String line;

        //log.info("loading posDic...");
        BufferedReader posBr = new BufferedReader(new InputStreamReader(new FileInputStream(posDic), "utf-8"));
        while((line = posBr.readLine()) != null) {
            pos.add(line);
        }
        posBr.close();
        //log.info("loading posDic success...");

        //log.info("loading negDic...");
        BufferedReader negBr = new BufferedReader(new InputStreamReader(new FileInputStream(negDic), "utf-8"));
        while((line = negBr.readLine()) != null) {
            neg.add(line);
        }
        negBr.close();
        //log.info("loading negDic success...");

        //log.info("loading negWordDic...");
        BufferedReader negWordBr = new BufferedReader(new InputStreamReader(new FileInputStream(negWordDic), "utf-8"));
        while((line = negWordBr.readLine()) != null) {
            negWord.add(line);
        }
        negWordBr.close();
        //log.info("loading negWordDic success...");

        testFile = Analysis.class.getResource("/sentiment/test.txt").getPath();

        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainResultPlusMapper sentimentTrainResultPlusMapper = session.getMapper(SentimentTrainResultPlusMapper.class);

        List<TrainResultPlus> negR = sentimentTrainResultPlusMapper.queryAllByXType(0, x2);
        List<TrainResultPlus> unsR = sentimentTrainResultPlusMapper.queryAllByXType(1, x2);
        List<TrainResultPlus> posR = sentimentTrainResultPlusMapper.queryAllByXType(2, x2);
        session.commit();

        negWeight = parseBool(negR);
        posWeight = parseBool(posR);
        unsWeight = parseBool(unsR);
        //log.info("loading model success...");

        //log.info("count Doc");
        SentimentTrainMapper trainMapper = session.getMapper(SentimentTrainMapper.class);
        posDocNum = trainMapper.querySizeByWeight(1f);
        negDocNum = trainMapper.querySizeByWeight(0f);
        unsDocNum = trainMapper.querySizeByWeight(0.5f);
        session.commit();
        session.close();
        //log.info("count Doc success... pos:" + posDocNum + " neg:" + negDocNum + " uns:" + unsDocNum);
    }

    /**
     * 转化
     * @param list
     * @return
     */
    private HashMap<String, Integer> parsePlus(List<TrainResultPlus> list) {
        HashMap<String, Integer> map = new HashMap<>();
        for(TrainResultPlus t: list){
            map.put(t.getWord(), t.getWeight());
        }
        return map;
    }

    /**
     * 转化
     * @param list
     * @return
     */
    private HashMap<String, Integer> parseBool(List<TrainResultPlus> list) {
        HashMap<String, Integer> map = new HashMap<>();
        for(TrainResultPlus t: list){
            map.put(t.getWord(), 1);
        }
        return map;
    }

    /**
     * 转化
     * @param list
     * @return
     */
    private HashMap<String, Integer> parse(List<TrainResult> list){
        HashMap<String, Integer> map = new HashMap<>();
        for(TrainResult t: list){
            map.put(t.getWord(), t.getWeight());
        }
        return map;
    }

    public void testData(int method) throws IOException {
        List<Sentiment> list = this.readTestData();
        int countP = 0;
        int countN = 0;
        int countU = 0;
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTestMapper sentimentTestMapper = session.getMapper(SentimentTestMapper.class);
        for(int i = 0; i < list.size(); i++) {
            Sentiment sentiment = list.get(i);
            String sentence = list.get(i).getContent();
            int ans = -1;
            switch (method) {
                case 0:
                    ans = this.classfy(sentence);
                    break;
                case 1:
                    ans = this.classfyWithNeg(sentence);
                    break;
                default:
                    ans = this.classfy(sentence);
                    break;
            }
            if (ans == 2) {
                sentiment.setWeight(1f);
                countP++;
            } else if(ans == 0) {
                sentiment.setWeight(0f);
                countN++;
            } else {
                sentiment.setWeight(0.5f);
                countU++;
            }
            sentimentTestMapper.updateAns(sentiment);
            session.commit();
        }
        log.info("P:" + countP + " N:" + countN + " U:" + countU);
        session.close();
    }

    /**
     * 分类
     * @param weibo
     */
    public int classfy(String weibo) throws IOException {
        HashMap<String, Integer> map = getSentimentWord(weibo);

        double negativeAns = 1, positiveAns = 1, unsureAns = 1;

        for(Map.Entry<String, Integer> e: map.entrySet()) {
            String word = e.getKey();
            negativeAns *= Math.pow(this.postProbability(word, negWeight), e.getValue());
        }
        negativeAns *= this.priorProbability(negDocNum);
        log.info("negativeAns: " + negativeAns);

        for(Map.Entry<String, Integer> e: map.entrySet()) {
            String word = e.getKey();
            positiveAns *= Math.pow(this.postProbability(word, posWeight), e.getValue());
        }
        positiveAns *= this.priorProbability(posDocNum);
        log.info("positiveAns: " + positiveAns);

        for(Map.Entry<String, Integer> e: map.entrySet()) {
            String word = e.getKey();
            unsureAns *= Math.pow(this.postProbability(word, unsWeight), e.getValue());
        }
        unsureAns *= this.priorProbability(unsDocNum);
        log.info("unsureAns: " + unsureAns);

        //return (negativeAns - positiveAns);
        if(negativeAns > positiveAns && negativeAns > unsureAns)
            return 0;
        else if(positiveAns > unsureAns)
            return 2;
        else
            return 1;
    }

    /**
     * 分类
     * @param weibo
     * @return
     * @throws IOException
     */
    public int classfyWithNeg(String weibo) throws IOException {
        HashMap<String, Integer> map = getSentimentWordWithNeg(weibo);

        double negativeAns = 1, positiveAns = 1, unsureAns = 1;

        for(Map.Entry<String, Integer> e: map.entrySet()) {
            String word = e.getKey();
            negativeAns *= Math.pow(this.postProbability(word, negWeight), e.getValue());
        }
        negativeAns *= this.priorProbability(negDocNum);
        log.info("negativeAns: " + negativeAns);

        for(Map.Entry<String, Integer> e: map.entrySet()) {
            String word = e.getKey();
            positiveAns *= Math.pow(this.postProbability(word, posWeight), e.getValue());
        }
        positiveAns *= this.priorProbability(posDocNum);
        log.info("positiveAns: " + positiveAns);

        for(Map.Entry<String, Integer> e: map.entrySet()) {
            String word = e.getKey();
            unsureAns *= Math.pow(this.postProbability(word, unsWeight), e.getValue());
        }
        unsureAns *= this.priorProbability(unsDocNum);
        log.info("unsureAns: " + unsureAns);

        //return (negativeAns - positiveAns);
        if(negativeAns > positiveAns && negativeAns > unsureAns)
            return 0;
        else if(positiveAns > unsureAns)
            return 2;
        else
            return 1;
    }

    /**
     * 提取情感词
     * @param weibo
     * @return
     * @throws IOException
     */
    private HashMap<String, Integer> getSentimentWord(String weibo) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();
        String segSentence = seg.seg(weibo);
        log.info(segSentence);
        String[] words = segSentence.split("\\|");
        for(String w: words) {
            if(w.length() > 1 && (neg.contains(w) || pos.contains(w))) {
                log.info(w);
                if(map.containsKey(w)){
                    map.put(w, map.get(w) + 1);
                } else {
                    map.put(w, 1);
                }
            }
        }
        return map;
    }

    /**
     * 提取情感词（结合否定词）
     * @param weibo
     * @return
     * @throws IOException
     */
    private HashMap<String, Integer> getSentimentWordWithNeg(String weibo) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();
        String segSentence = seg.seg(weibo);
        log.info(segSentence);
        String[] words = segSentence.split("\\|");
        int negFlag = 0;
        for(String w: words) {
            if(negWord.contains(w)) {
                negFlag++;
            }
            if(w.length() > 1 && (neg.contains(w) || pos.contains(w)) && !negWord.contains(w)) {
                if(negFlag % 2 == 1)
                    w = "@" + w;
                negFlag = 0;
                log.info(w);
                if(map.containsKey(w)){
                    map.put(w, map.get(w) + 1);
                } else {
                    map.put(w, 1);
                }
            }
        }
        return map;
    }

    /**
     * 读取测试集
     * @param path
     * @return
     * @throws IOException
     */
    private List<Sentiment> readTestData(String path) throws IOException {
        this.testFile = path;
        return readTestData();
    }

    /**
     * 读取测试集
     * @return
     * @throws IOException
     */
    private List<Sentiment> readTestData() throws IOException {
        File file = new File(testFile);
        if(!file.exists()) {
            log.error("File: " + this.testFile + " is not exist...");
        }

        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTestMapper testMapper = session.getMapper(SentimentTestMapper.class);
        int size = testMapper.querySize();
        log.info("test data size: " + size);
        if(size == 0) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while((line = br.readLine()) != null){
                String[] infos = line.split("\\t");
                if(infos.length != 2)
                    continue;
                Sentiment sentiment = new Sentiment(Integer.parseInt(infos[0]), infos[1], -1f);
                testMapper.insert(sentiment);
            }
            session.commit();
        }
        List<Sentiment> list = testMapper.queryAll();
        session.commit();
        session.close();
        return list;
    }

    private double priorProbability(int SentimentDoc) {
        double ans = 1;

        ans = (double)SentimentDoc/((double)negDocNum + (double)posDocNum + (double)unsDocNum);

        return ans;
    }

    private double postProbability(String word, HashMap<String, Integer> sentimentWeight) {
        double ans, V, E;
        double weight = 0;
        double weights = 0;

        if (sentimentWeight.containsKey(word))
            weight = (double)sentimentWeight.get(word);

        weights = postWeight(sentimentWeight);

        V = postWeight(negWeight) + postWeight(posWeight) + postWeight(unsWeight);
        E = 1 / Math.abs(V);

        ans = (weight + E) / (weights + E * Math.abs(V));

        return ans;
    }

    private double postWeight(HashMap<String, Integer> sentimentWeight) {
        double weight = 0;
        for(Map.Entry<String, Integer> e: sentimentWeight.entrySet()) {
            weight += (double) e.getValue();
        }
        return  weight;
    }

    public static void main(String[] args) throws IOException {
        //Analysis analysis = new Analysis(0, true);
        //String weibo = "幽静的桃江路上，有一家法式薄饼餐厅#La Creperie#，每次经过几乎总是客座满盈。法国老板热情又好客，主打可丽饼，也有法国空运来的生蚝和法国葡萄酒。Crepes有近百种口味和配料可供选择，酱料超级丰富，咸甜味皆有。Lunch/Brunch Set 量都很大。@空气精灵de美味食光";
        String weibo = "哈哈";
        Analysis analysis = new Analysis(0);

        int flag = analysis.classfyWithNeg(weibo);
        log.info(flag);
        //analysis.testData(1);
    }
}
