package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.TrainResult;

import java.util.List;

/**
 * Created by tanjie on 10/16/15.
 */
public interface SentimentTrainResultMapper {
    /**
     * 插入
     * @param result
     */
    void insert(TrainResult result);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 按类查询
     * @param type
     * @return
     */
    List<TrainResult> queryResultByType(int type);
}
