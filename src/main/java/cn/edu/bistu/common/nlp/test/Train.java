package cn.edu.bistu.common.nlp.test;

import cn.edu.bistu.common.nlp.libsvm.svm.svm_node;
import cn.edu.bistu.weibo.dao.SentimentTrainMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Sentiment;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanjie on 10/24/15.
 */
public class Train {
    protected static Logger log = Logger.getLogger(Train.class);

    private static String dirPath = Train.class.getResource("/sentiment/").getPath();

    HashSet<String> negDic, posDic, negModelDic, posModelDic, nWordDic;

    HashSet<String> emo, negEmo, posEmo;

    public Train() throws IOException {
        String negWord = dirPath + "sentiment_dic/hownet/negWord.txt";
        String posWord = dirPath + "sentiment_dic/hownet/posWord.txt";
        String negModel = dirPath + "sentiment_dic/hownet/negModel.txt";
        String posModel = dirPath + "sentiment_dic/hownet/posModel.txt";
        String nWord = dirPath + "sentiment_dic/negWord.txt";
        String emof = dirPath + "sentiment_dic/emotion.txt";
        String negEmof = dirPath + "sentiment_dic/negEmo.txt";
        String posEmof = dirPath + "sentiment_dic/posEmo.txt";
        String line;
        BufferedReader reader = null;

        negDic = new HashSet<>();
        posDic = new HashSet<>();
        negModelDic = new HashSet<>();
        posModelDic = new HashSet<>();
        nWordDic = new HashSet<>();
        emo = new HashSet<>();
        negEmo = new HashSet<>();
        posEmo = new HashSet<>();

        log.info("初始化...加载词典...");
        reader = new BufferedReader(new FileReader(negWord));
        while((line = reader.readLine()) != null) {
            negDic.add(line.trim());
        }
        reader.close();

        reader = new BufferedReader(new FileReader(posWord));
        while((line = reader.readLine()) != null) {
            posDic.add(line.trim());
        }
        reader.close();

        reader = new BufferedReader(new FileReader(negModel));
        while((line = reader.readLine()) != null) {
            negModelDic.add(line.trim());
        }
        reader.close();

        reader = new BufferedReader(new FileReader(posModel));
        while((line = reader.readLine()) != null) {
            posModelDic.add(line.trim());
        }
        reader.close();

        reader = new BufferedReader(new FileReader(nWord));
        while((line = reader.readLine()) != null) {
            nWordDic.add(line.trim());
        }
        reader.close();

        reader = new BufferedReader(new FileReader(emof));
        while((line = reader.readLine()) != null) {
            emo.add(line.trim());
        }
        reader.close();
        reader = new BufferedReader(new FileReader(negEmof));
        while((line = reader.readLine()) != null) {
            negEmo.add(line.trim());
        }
        reader.close();
        reader = new BufferedReader(new FileReader(posEmof));
        while((line = reader.readLine()) != null) {
            posEmo.add(line.trim());
        }
        reader.close();
        log.info("加载完成");
    }

    private String createTrainFile() throws IOException {
//        String emofile = dirPath + "emo.txt";
//        FileWriter emowr = new FileWriter(emofile);
//        List<String> emol = new ArrayList<>();

        String dataFile = dirPath + "svm_data/train.data";
        log.info(dataFile);
        File file = new File(dataFile);
        if(file.exists())
            file.delete();
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainMapper sentimentTrainMapper = session.getMapper(SentimentTrainMapper.class);

        List<Sentiment> sentiments = sentimentTrainMapper.queryAll();
        Pattern pattern = Pattern.compile("\\[[a-z0-9\\u4e00-\\u9fa5]+\\]");

        int negTotal = 0;
        int posTotal = 0;
        for(Sentiment sentiment: sentiments) {
            int label;
            if(sentiment.getWeight() == 1f) {
                posTotal++;
                label = 2;
            }
            else if(sentiment.getWeight() == 0f) {
                negTotal++;
                label = 0;
            }
            else
                continue;
                //label = 1;
            String line = label + " ";

            String sentence = sentiment.getContent();
            Matcher m = pattern.matcher(sentence);

            boolean hasEmotion = false;
            int negNum = 0;
            int posNum = 0;
            while(m.find()) {
                String emoStr = m.group();
                if(emo.contains(emoStr)) {
                    hasEmotion = true;
                } else {
                    continue;
                }
                if(negEmo.contains(emoStr))
                    negNum++;
                else if(posEmo.contains(emoStr))
                    posNum++;
                else {
                    List<Term> emoWords = ToAnalysis.parse(emoStr.substring(1, emoStr.length()-1));
                    for(Term t: emoWords) {
                        if(posDic.contains(t.getName())) {
                            posNum++;
                            continue;
                        } else if(negDic.contains(t.getName())) {
                            negNum++;
                            continue;
                        }
                    }
                }
            }
            if(hasEmotion) {
                line += "1:1" + " " + "2:" + posNum + " " + "3:" + negNum + " ";
            } else {
                line += "1:0" + " " + "2:0" + " " + "3:0" + " ";
            }

            List<Term> words = ToAnalysis.parse(sentence);

            int neg = 0;    //负面情感词
            int pos = 0;    //正面情感词
            int posM = 0;   //正面评价词
            int negM = 0;   //负面评价词
            int nW = 0;     //否定词
            for(Term word: words) {
                String w = word.getName();
                if(nWordDic.contains(w))
                    nW++;
                if(negDic.contains(w))
                    neg++;
                if(posDic.contains(w))
                    pos++;
                if(negModelDic.contains(w))
                    negM++;
                if(posModelDic.contains(w))
                    posM++;
            }
            line += "4:" + pos + " " + "5:" + neg + " " + "6:" + posM + " " + "7:" + negM + " " + "8:" + nW + "\r\n";
            writer.write(line);
        }
        writer.close();
//        for(String s: emol) {
//            emowr.write(s + "\r\n");
//        }
//        emowr.close();
        return dataFile;
    }

    private svm_node[] predict(String weibo) {
        svm_node[] nodes = new svm_node[8];

        Pattern pattern = Pattern.compile("\\[[a-z0-9\\u4e00-\\u9fa5]+\\]");
        Matcher m = pattern.matcher(weibo);

        boolean hasEmotion = false;
        int negNum = 0;
        int posNum = 0;
        while(m.find()) {
            String emoStr = m.group();
            if(emo.contains(emoStr)) {
                hasEmotion = true;
            } else {
                continue;
            }
            if(negEmo.contains(emoStr))
                negNum++;
            else if(posEmo.contains(emoStr))
                posNum++;
            else {
                List<Term> emoWords = ToAnalysis.parse(emoStr.substring(1, emoStr.length()-1));
                for(Term t: emoWords) {
                    if(posDic.contains(t.getName())) {
                        posNum++;
                        continue;
                    } else if(negDic.contains(t.getName())) {
                        negNum++;
                        continue;
                    }
                }
            }
        }
        nodes[0] = new svm_node();
        nodes[1] = new svm_node();
        nodes[2] = new svm_node();
        if(hasEmotion) {
            nodes[0].index = 1;
            nodes[0].value = 1;
            nodes[1].index = 2;
            nodes[1].value = posNum;
            nodes[2].index = 1;
            nodes[2].value = negNum;
        } else {
            nodes[0].index = 1;
            nodes[0].value = 0;
            nodes[1].index = 2;
            nodes[1].value = 0;
            nodes[2].index = 3;
            nodes[2].value = 0;
        }

        List<Term> words = ToAnalysis.parse(weibo);

        int neg = 0;    //负面情感词
        int pos = 0;    //正面情感词
        int posM = 0;   //正面评价词
        int negM = 0;   //负面评价词
        int nW = 0;     //否定词
        for(Term word: words) {
            String w = word.getName();
            if(nWordDic.contains(w))
                nW++;
            if(negDic.contains(w))
                neg++;
            if(posDic.contains(w))
                pos++;
            if(negModelDic.contains(w))
                negM++;
            if(posModelDic.contains(w))
                posM++;
        }
        nodes[3] = new svm_node();
        nodes[4] = new svm_node();
        nodes[5] = new svm_node();
        nodes[6] = new svm_node();
        nodes[7] = new svm_node();

        nodes[3].index = 4;
        nodes[3].value = pos;
        nodes[4].index = 5;
        nodes[4].value = neg;
        nodes[5].index = 6;
        nodes[5].value = posM;
        nodes[6].index = 7;
        nodes[6].value = negM;
        nodes[7].index = 8;
        nodes[7].value = nW;
        log.info(Arrays.toString(nodes));
        return nodes;
    }

    public static void main(String[] args) throws IOException {
        Train train = new Train();
        String file = train.createTrainFile();

//        svm_predict svmPredict = new svm_predict();
//        String dataFile = dirPath + "svm_data/train.data";
//        String modelFile = dirPath + "svm_data/model.data";
//        log.info(dataFile);
//        //String[] argvTrain = {dataFile, modelFile};
//        //svm_train.main(argvTrain);
//        svm_model svmModel = svm.svm_load_model(modelFile);
//        String weibo = "我很开心";
//        System.out.println(svm.svm_predict(svmModel, train.predict(weibo)));
    }
}
