package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.Rank;
import cn.edu.bistu.weibo.model.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 12/21/15.
 */
public interface RankMapper {
    /**
     * 更新数据
     * @param userid
     * @param rank
     * @param valueRank
     */
    void updateRank(@Param("id") String userid,
                    @Param("rank") double rank,
                    @Param("valueRank") double valueRank);

    /**
     * 重置rank
     */
    void resetRank();

    /**
     * 查询所有
     * @return
     */
    List<Rank> queryAll();

    /**
     * 查询重置数量
     * @return
     */
    int resetCount();

    /**
     * 分页查询
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @param search
     * @return
     */
    List<Rank> queryInPage(@Param("field") String field,
                            @Param("asc") String asc,
                            @Param("pn") int pn,
                            @Param("cp") int cp,
                            @Param("search") String search);

    /**
     * 查询数量
     * @param search
     * @return
     */
    int querySize(String search);

    /**
     * 查询
     * @param userid
     * @return
     */
    Rank queryById(String userid);
}
