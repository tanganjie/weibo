package cn.edu.bistu.common.usercredit;

import cn.edu.bistu.weibo.dao.RankMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Rank;
import cn.edu.bistu.weibo.model.Users;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

/**
 * Created by tanjie on 12/20/15.
 */
public class UserCreditCalculate {
    protected static Logger logger = Logger.getLogger(UserCreditCalculate.class);

    /**
     * 基本信息完整度
     * @param user
     * @return
     */
    public static double infoComlete(Users user) {
        int count=0;
        if(!StringUtils.isEmpty(user.getName()))
            count++;
        if(!StringUtils.isEmpty(user.getSex()))
            count++;
        if(!StringUtils.isEmpty(user.getSummary()))
            count++;
        if(!StringUtils.isEmpty(user.getBirthday()))
            count++;
        if(!StringUtils.isEmpty(user.getArea()))
            count++;
        if(!StringUtils.isEmpty(user.getTag()))
            count++;
        if(!StringUtils.isEmpty(user.getOfficial()))
            count++;
        return ((double)count)/7.0;
    }

    /**
     * 活跃度
     * @param user
     * @return
     */
    public static double activeCal(Users user) {
        return ((double)user.getWeibos())/2000.0;
    }

    /**
     * 传播影响力
     * @param user
     * @return
     */
    public static double rankCal(Users user) {
        SqlSession session = SessionFactory.getFactory().openSession();
        RankMapper rankMapper = session.getMapper(RankMapper.class);

        Rank rank = rankMapper.queryById(user.getId());

        session.close();
        return rank.getValuerank();
    }

    /**
     * 平台认证
     * @param user
     * @return
     */
    public static double platFormCal(Users user) {
        return StringUtils.isEmpty(user.getOfficial())?0.3:0.8;
    }
}
