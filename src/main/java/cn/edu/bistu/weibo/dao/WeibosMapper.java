package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.Weibos;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 12/21/15.
 */
public interface WeibosMapper {
    /**
     * 插入
     * @param weibos
     */
    void insert(Weibos weibos);

    /**
     * 删除用户微博
     * @param userid
     */
    void deleteByUserId(String userid);

    /**
     * 查询用户微博
     * @param userid
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @param search
     * @return
     */
    List<Weibos> queryWeibosByUserId(@Param("userid") String userid,
                                     @Param("field") String field,
                                     @Param("asc") String asc,
                                     @Param("pn") int pn,
                                     @Param("cp") int cp,
                                     @Param("search") String search);

    /**
     * 查询计数
     * @param userid
     * @param search
     * @return
     */
    int queryWeibosSize(@Param("userid") String userid, @Param("search") String search);

    /**
     *  查询
     * @param id
     * @return
     */
    Weibos queryById(int id);

    List<Weibos> queryAll();

    List<Weibos> queryByUserId(String userid);

}
