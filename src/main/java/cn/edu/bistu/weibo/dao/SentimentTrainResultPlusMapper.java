package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.TrainResultPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 10/26/15.
 */
public interface SentimentTrainResultPlusMapper {
    /**
     * 插入
     * @param trainResultPlus
     */
    void insert(TrainResultPlus trainResultPlus);

    /**
     * 查询所有
     * @return
     */
    List<TrainResultPlus> queryAll();

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 按类查询
     * @param type
     * @return
     */
    List<TrainResultPlus> queryAllByType(int type);

    /**
     * 按类查找大于阈值x2
     * @param type
     * @param x2
     * @return
     */
    List<TrainResultPlus> queryAllByXType(@Param("type") int type, @Param("x2") double x2);
}
