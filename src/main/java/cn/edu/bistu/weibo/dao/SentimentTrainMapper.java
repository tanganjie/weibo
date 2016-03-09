package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.Sentiment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 10/16/15.
 */
public interface SentimentTrainMapper {
    /**
     * 插入
     * @param sentiment
     */
    void insert(Sentiment sentiment);

    /**
     * 清空表
     */
    void deleteAll();

    /**
     * 查询数量
     * @return
     */
    int querySize();

    /**
     * 查询查找数量
     * @param search
     * @return
     */
    int querySearchSize(String search);

    /**
     * 查询所有训练数据
     * @return
     */
    List<Sentiment> queryAll();

    /**
     * 分页查询
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @return
     */
    List<Sentiment> queryInPage(@Param("field") String field,
                                @Param("asc") String asc,
                                @Param("pn") int pn,
                                @Param("cp") int cp,
                                @Param("search") String search);

    /**
     * 按类查询
     * @param weight
     * @return
     */
    int querySizeByWeight(float weight);
}
