package cn.edu.bistu.common.nlp.util;

/**
 * Created by tanjie on 10/15/15.
 */
public class WordSeg {
    private final static Segment seg = new Segment();

    public static Segment getInstance() {
        return seg;
    }
}