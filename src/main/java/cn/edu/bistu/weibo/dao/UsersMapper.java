package cn.edu.bistu.weibo.dao;

import cn.edu.bistu.weibo.model.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanjie on 12/18/15.
 */
public interface UsersMapper {
    /**
     * 插入
     * @param user
     */
    void insert(Users user);

    /**
     * 查询所有
     * @return
     */
    List<Users> queryUsers();

    /**
     * 查询
     * @param id
     * @return
     */
    Users queryUserById(String id);

    /**
     * 删除用户
     * @param id
     */
    void deleteUsersById(String id);

    /**
     * 分页查询
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @param search
     * @return
     */
    List<Users> queryInPage(@Param("field") String field,
                            @Param("asc") String asc,
                            @Param("pn") int pn,
                            @Param("cp") int cp,
                            @Param("search") String search);

    /**
     * 查询计数
     * @param search
     * @return
     */
    int queryUsersSize(String search);

    int countUsers(String sex);
}
