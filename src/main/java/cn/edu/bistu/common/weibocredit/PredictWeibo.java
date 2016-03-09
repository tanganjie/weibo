package cn.edu.bistu.common.weibocredit;

import cn.edu.bistu.common.nlp.libsvm.svm.svm;

import java.io.IOException;

/**
 * Created by tanjie on 12/23/15.
 */
public class PredictWeibo {
    private static String dirPath = TrainData.class.getResource("/credibility/").getPath();
    public static int predict(String id, String w) throws IOException {
        String modelFile = dirPath + "svm/train.model";
        WeiboCreditCalculate weiboCreditCalculate = new WeiboCreditCalculate(id, w);
        int x = (int) svm.svm_predict(svm.svm_load_model(modelFile), weiboCreditCalculate.parseNodes());
        if(w.startsWith("转发"))
            x=0;
        return x;
    }

    public static double calCredit(int c, double uCredit) {
        double v=0;
        if(uCredit<0.6){
            v=2*0.6/(1+Math.exp(10*(uCredit-0.6))) + 2*0.4-1;
        }else {
            v=2*0.4/(1+Math.exp(10*(uCredit-0.6)));
        }
        //谣言
        if(c==1){
            return 0.5-0.5*v;
        }else {
            return 0.5+0.5*(1-v);
        }
    }
}
