package cn.edu.bistu.common.weibocredit;

import cn.edu.bistu.common.nlp.libsvm.svm.svm;
import cn.edu.bistu.common.nlp.libsvm.svm.svm_node;
import cn.edu.bistu.common.nlp.libsvm.svm_predict;

import java.io.IOException;

/**
 * Created by tanjie on 12/22/15.
 */
public class TrainData {
    private static String dirPath = TrainData.class.getResource("/credibility/").getPath();
    public static void main(String[] args) throws IOException {
        //String dataFile = dirPath + "data/train.data";
        //String[] argvTrain = {dataFile, "/Users/tanjie/work/train.model"};
        //svm_train.main(argvTrain);

//        String dataFile = dirPath + "data/test.data";
        String modelFile = dirPath + "svm/train.model";
//        String[] argvTest = {dataFile, modelFile, "/Users/tanjie/work/result.txt"};
//        svm_predict.main(argvTest);

        svm_node[] nodes = new svm_node[9];
        for(int i=0;i<9;i++){
            nodes[i]=new svm_node();
        }
        nodes[0].index = 1;
        nodes[0].value = 0;
        nodes[1].index = 2;
        nodes[1].value = 0;
        nodes[2].index = 3;
        nodes[2].value = 0;
        nodes[3].index = 4;
        nodes[3].value = 0;
        nodes[4].index = 5;
        nodes[4].value = 0;
        nodes[5].index = 6;
        nodes[5].value = 0;
        nodes[6].index = 7;
        nodes[6].value = 0;
        nodes[7].index = 8;
        nodes[7].value = 0;
        nodes[8].index = 9;
        nodes[8].value = 0;
        System.out.println(svm.svm_predict(svm.svm_load_model(modelFile), nodes));
    }
}
