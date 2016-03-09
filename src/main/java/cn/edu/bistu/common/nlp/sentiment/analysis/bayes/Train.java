package cn.edu.bistu.common.nlp.sentiment.analysis.bayes;

import cn.edu.bistu.common.nlp.util.Segment;
import cn.edu.bistu.common.nlp.util.WordSeg;
import cn.edu.bistu.weibo.dao.SentimentTrainMapper;
import cn.edu.bistu.weibo.dao.SentimentTrainResultMapper;
import cn.edu.bistu.weibo.dao.SentimentTrainResultPlusMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Sentiment;
import cn.edu.bistu.weibo.model.TrainResult;
import cn.edu.bistu.weibo.model.TrainResultPlus;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by tanjie on 10/16/15.
 */
public class Train {
    protected static Logger log = Logger.getLogger(Train.class);

    //分词器
    private Segment seg = WordSeg.getInstance();
    //情感词典
    private static HashSet<String> neg, pos;
    //否定词集
    private static HashSet<String> negWord;
    //情感句数
    //private static Integer negDoc, posDoc, unsDoc;
    //词与权值
    private static HashMap<String, Integer> negWeight, posWeight, unsWeight;

    private String trainFile;

    public Train() throws IOException {
        String dicPath = Train.class.getResource("/sentiment/sentiment_dic/").getPath();
        log.info(dicPath);
        String posDic = dicPath + "ntusd-positive.txt";
        String negDic = dicPath + "ntusd-negative.txt";
        String negWordDic = dicPath + "negWord.txt";

        pos = new HashSet<>();
        neg = new HashSet<>();
        negWord = new HashSet<>();

        String line;

        log.info("loading posDic...");
        BufferedReader posBr = new BufferedReader(new FileReader(posDic));
        while((line = posBr.readLine()) != null) {
            pos.add(line);
        }
        posBr.close();
        log.info("loading posDic success...");

        log.info("loading negDic...");
        BufferedReader negBr = new BufferedReader(new FileReader(negDic));
        while((line = negBr.readLine()) != null) {
            neg.add(line);
        }
        negBr.close();
        log.info("loading negDic success...");

        log.info("loading negWordDic...");
        BufferedReader negWordBr = new BufferedReader(new FileReader(negWordDic));
        while((line = negWordBr.readLine()) != null) {
            negWord.add(line);
        }
        negWordBr.close();
        log.info("loading negWordDic success...");

        trainFile = Train.class.getResource("/sentiment/train.txt").getPath();
    }

    public void trainCorpus(String path) throws IOException {
        this.trainFile = path;
        this.trainCorpus();
    }

    public void trainCorpus() throws IOException {
        File file = new File(trainFile);
        if(!file.exists()) {
            log.error("File: " + this.trainFile + " is not exist...");
        }

        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainMapper sentimentTrainMapper = session.getMapper(SentimentTrainMapper.class);
        int size = sentimentTrainMapper.querySize();
        log.info("count: " + size);
        if(size == 0){
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                log.info(line);
                String[] infos = line.split("\\t");
                if(infos.length != 3)
                    continue;
                Sentiment sentiment = new Sentiment(Integer.parseInt(infos[0]), infos[2], Float.parseFloat(infos[1]));
                sentimentTrainMapper.insert(sentiment);
            }
            session.commit();
        }

        List<Sentiment> list = sentimentTrainMapper.queryAll();
        session.commit();

        log.info("query: " + list.size());

        posWeight = new HashMap<>();
        negWeight = new HashMap<>();
        unsWeight = new HashMap<>();

        for(Sentiment s: list) {
            String content = s.getContent();
            String segContent = seg.seg(content);
            String[] words = segContent.split("\\|");
            for(String w: words) {
                if(w.length() > 1 && (pos.contains(w) || neg.contains(w))){
                    if(s.getWeight() == 0f) {
                        if(negWeight.containsKey(w)){
                            Integer weight = negWeight.get(w);
                            negWeight.put(w, weight + 1);
                        } else {
                            negWeight.put(w, 1);
                        }
                    } else if(s.getWeight() == 1f) {
                        if(posWeight.containsKey(w)){
                            Integer weight = posWeight.get(w);
                            posWeight.put(w, weight + 1);
                        } else {
                            posWeight.put(w, 1);
                        }
                    } else {
                        if(unsWeight.containsKey(w)){
                            Integer weight = unsWeight.get(w);
                            unsWeight.put(w, weight + 1);
                        } else {
                            unsWeight.put(w, 1);
                        }
                    }
                }
            }
        }
        log.info("pos: " + posWeight.size());
        log.info("neg: " + negWeight.size());
        log.info("uns: " + unsWeight.size());

        SentimentTrainResultMapper sentimentTrainResultMapper = session.getMapper(SentimentTrainResultMapper.class);
        sentimentTrainResultMapper.deleteAll();
        session.commit();

        for(Iterator iter = posWeight.entrySet().iterator(); iter.hasNext();){
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) iter.next();
            TrainResult result = new TrainResult(2, e.getKey(), e.getValue());
            sentimentTrainResultMapper.insert(result);
            session.commit();
        }

        for(Iterator iter = negWeight.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) iter.next();
            TrainResult result = new TrainResult(0, e.getKey(), e.getValue());
            sentimentTrainResultMapper.insert(result);
            session.commit();
        }

        for(Iterator iter = unsWeight.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) iter.next();
            TrainResult result = new TrainResult(1, e.getKey(), e.getValue());
            sentimentTrainResultMapper.insert(result);
            session.commit();
        }
        session.close();
    }

    /**
     * 加入否定词训练
     * @throws IOException
     */
    public void trainCorpusPlus() throws IOException {
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainMapper sentimentTrainMapper = session.getMapper(SentimentTrainMapper.class);

        List<Sentiment> list = sentimentTrainMapper.queryAll();
        session.commit();

        posWeight = new HashMap<>();
        negWeight = new HashMap<>();
        unsWeight = new HashMap<>();

        for (Sentiment sentiment: list) {
            String content = sentiment.getContent();
            //String segContent = seg.seg(content);
            //String[] words = segContent.split("\\|");

            List<Term> words = ToAnalysis.parse(content);

            int negFlag = 0; //否定标志
            for(Term word: words) {
                String w = word.getName();
                if (negWord.contains(w)){
                    negFlag++;
                }
                if(w.length() > 1 && (pos.contains(w) || neg.contains(w)) && !negWord.contains(w)){
                    if(sentiment.getWeight() == 0f) {
                        if(negFlag % 2 == 1) {
                            w = "@" + w;
                        }
                        negFlag = 0;
                        if(negWeight.containsKey(w)){
                            Integer weight = negWeight.get(w);
                            negWeight.put(w, weight + 1);
                        } else {
                            negWeight.put(w, 1);
                        }
                    } else if(sentiment.getWeight() == 1f) {
                        if(posWeight.containsKey(w)){
                            Integer weight = posWeight.get(w);
                            posWeight.put(w, weight + 1);
                        } else {
                            posWeight.put(w, 1);
                        }
                    } else {
                        if(unsWeight.containsKey(w)){
                            Integer weight = unsWeight.get(w);
                            unsWeight.put(w, weight + 1);
                        } else {
                            unsWeight.put(w, 1);
                        }
                    }
                }
            }
        }

        SentimentTrainResultPlusMapper sentimentTrainResultPlusMapper = session.getMapper(SentimentTrainResultPlusMapper.class);
        for(Map.Entry<String, Integer> e: negWeight.entrySet()) {
            double x2 = 0;

            double A = (double)e.getValue();

            double B = 0;
            if(unsWeight.get(e.getKey()) != null)
                B = B + (double)unsWeight.get(e.getKey());
            if(posWeight.get(e.getKey()) != null)
                B = B + (double)posWeight.get(e.getKey());

            double C = 0;
            for(Map.Entry<String, Integer> x: negWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    C = C + (double)x.getValue();
            }

            double D = 0;
            for(Map.Entry<String, Integer> x: unsWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    D = D + (double)x.getValue();
            }
            for(Map.Entry<String, Integer> x: posWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    D = D + (double)x.getValue();
            }


            x2 = (double)list.size() * Math.pow((A * D - C * B), 2);
            x2 = x2 / (A + B) / (A + C) / (B + D) / (C + D);

            TrainResultPlus trainResultPlus = new TrainResultPlus(0, e.getKey(), e.getValue(), x2);
            sentimentTrainResultPlusMapper.insert(trainResultPlus);
            session.commit();
        }

        for(Map.Entry<String, Integer> e: posWeight.entrySet()) {
            double x2 = 0;

            double A = (double)e.getValue();

            double B = 0;
            if(unsWeight.get(e.getKey()) != null)
                B = B + (double)unsWeight.get(e.getKey());
            if(negWeight.get(e.getKey()) != null)
                B = B + (double)negWeight.get(e.getKey());

            double C = 0;
            for(Map.Entry<String, Integer> x: posWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    C = C + (double)x.getValue();
            }

            double D = 0;
            for(Map.Entry<String, Integer> x: unsWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    D = D + (double)x.getValue();
            }
            for(Map.Entry<String, Integer> x: negWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    D = D + (double)x.getValue();
            }


            x2 = (double)list.size() * Math.pow((A * D - C * B), 2);
            x2 = x2 / (A + B) / (A + C) / (B + D) / (C + D);

            TrainResultPlus trainResultPlus = new TrainResultPlus(2, e.getKey(), e.getValue(), x2);
            sentimentTrainResultPlusMapper.insert(trainResultPlus);
            session.commit();
        }

        for(Map.Entry<String, Integer> e: unsWeight.entrySet()) {
            double x2 = 0;

            double A = (double)e.getValue();

            double B = 0;
            if(posWeight.get(e.getKey()) != null)
                B = B + (double)posWeight.get(e.getKey());
            if(negWeight.get(e.getKey()) != null)
                B = B + (double)negWeight.get(e.getKey());

            double C = 0;
            for(Map.Entry<String, Integer> x: unsWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    C = C + (double)x.getValue();
            }

            double D = 0;
            for(Map.Entry<String, Integer> x: posWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    D = D + (double)x.getValue();
            }
            for(Map.Entry<String, Integer> x: negWeight.entrySet()) {
                if(!x.getKey().equals(e.getKey()))
                    D = D + (double)x.getValue();
            }


            x2 = (double)list.size() * Math.pow((A * D - C * B), 2);
            x2 = x2 / (A + B) / (A + C) / (B + D) / (C + D);

            TrainResultPlus trainResultPlus = new TrainResultPlus(1, e.getKey(), e.getValue(), x2);
            sentimentTrainResultPlusMapper.insert(trainResultPlus);
            session.commit();
        }
        session.close();
    }


    public static void main(String[] args) throws IOException {
        Train train = new Train();
        train.trainCorpusPlus();
    }
}
