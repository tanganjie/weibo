package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.Sentiment;
import cn.edu.bistu.weibo.model.SentimentTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 10/20/15.
 */
public interface SentimentTestMapper {
    /**
     * 插入
     * @param sentiment
     */
    void insert(Sentiment sentiment);

    /**
     * 查询所有测试集
     * @return
     */
    List<Sentiment> queryAll();

    /**
     * 查询数量
     * @return
     */
    int querySize();

    /**
     * 更新测试结果
     * @param sentiment
     */
    void updateAns(Sentiment sentiment);

    /**
     * 更新正确值
     * @param id
     * @param correct
     */
    void updateCor(@Param("id") int id, @Param("correct") int correct);

    /**
     * 测试结果复位
     */
    void reset();

    /**
     * 按id查找
     * @param id
     * @return
     */
    Sentiment querySentimentById(int id);

    /**
     * 按id删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询
     * @return
     */
    List<SentimentTest> queryCompleteAll();

    /**
     * 删除
     * @param id
     */
    void deleteData(int id);

    /**
     * 分页查询
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @param search
     * @return
     */
    List<SentimentTest> queryInPage(@Param("field") String field,
                                    @Param("asc") String asc,
                                    @Param("pn") int pn,
                                    @Param("cp") int cp,
                                    @Param("search") String search);

    int queryPageSize(String search);
}
