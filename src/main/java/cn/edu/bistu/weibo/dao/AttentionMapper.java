package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.Attention;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 12/20/15.
 */
public interface AttentionMapper {
    /**
     * 插入
     * @param attention
     */
    void insert(Attention attention);

    /**
     * 查询
     * @param id
     * @return
     */
    Attention queryAttentionById(String id);

    /**
     * 删除
     * @param id
     */
    void deleteAttentionById(String id);

    /**
     * 随机查询
     * @param n
     * @param userid
     * @return
     */
    List<Attention> queryByRandom(@Param("num") int n, @Param("userid") String userid);

    /**
     * 查询所有
     * @return
     */
    List<Attention> queryAll();
}
