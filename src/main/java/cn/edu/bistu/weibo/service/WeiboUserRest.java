package cn.edu.bistu.weibo.service;

import cn.edu.bistu.common.socialnet.pagerank.UserRank;
import cn.edu.bistu.common.usercredit.UserCreditCalculate;
import cn.edu.bistu.weibo.dao.RankMapper;
import cn.edu.bistu.weibo.dao.UsersMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Rank;
import cn.edu.bistu.weibo.model.Users;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by tanjie on 12/10/15.
 */
@Path("/Users")
public class WeiboUserRest {
    protected static Logger logger = Logger.getLogger(WeiboUserRest.class);

    /**
     * 分页查询
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @param search
     * @return
     */
    @POST
    @Path("/queryUsersInPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryUsers(@FormParam("field") String field,
                      @FormParam("asc") String asc,
                      @FormParam("pn") int pn,
                      @FormParam("cp") int cp,
                      @FormParam("search") String search) {
        //logger.info(field);
        SqlSession session = SessionFactory.getFactory().openSession();
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);

        search = "%" + search + "%";
        int total = usersMapper.queryUsersSize(search);
        List<Users> users = usersMapper.queryInPage(field, asc, pn, cp, search);
        JSONArray array = JSONArray.fromObject(users);
        session.close();
        return new JSONObject().element("total", total).element("rows", array);
    }

    @POST
    @Path("/reset")
    @Produces("text/plain")
    public String resetRank(){
        SqlSession session = SessionFactory.getFactory().openSession();
        RankMapper rankMapper = session.getMapper(RankMapper.class);
        rankMapper.resetRank();
        session.commit();
        session.close();
        return "ok";
    }

    @POST
    @Path("/queryRankInPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryRankInPage(@FormParam("field") String field,
                                      @FormParam("asc") String asc,
                                      @FormParam("pn") int pn,
                                      @FormParam("cp") int cp,
                                      @FormParam("search") String search){
        SqlSession session = SessionFactory.getFactory().openSession();
        RankMapper rankMapper = session.getMapper(RankMapper.class);
        if(rankMapper.resetCount() != 0) {
            session.close();
            UserRank.calRank();
            session = SessionFactory.getFactory().openSession();
            rankMapper = session.getMapper(RankMapper.class);
        }
        search = "%" + search + "%";
        List<Rank> ranks = rankMapper.queryInPage(field, asc, pn, cp, search);
        int total = rankMapper.querySize(search);
        session.close();
        return new JSONObject().element("total", total).element("rows", JSONArray.fromObject(ranks));
    }

    @POST
    @Path("/queryUser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject getUserDetailInfo(@FormParam("userid") String userid) {
        SqlSession session = SessionFactory.getFactory().openSession();
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        Users user = usersMapper.queryUserById(userid);
        session.close();
        return JSONObject.fromObject(user);
    }

    @POST
    @Path("/calCredit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject calUserCredit(@FormParam("userid") String userid) {
        SqlSession session = SessionFactory.getFactory().openSession();
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        Users user = usersMapper.queryUserById(userid);
        session.close();

        double info = UserCreditCalculate.infoComlete(user);
        double trans = UserCreditCalculate.rankCal(user);
        double active = UserCreditCalculate.activeCal(user);
        double plat = UserCreditCalculate.platFormCal(user);
        double credit = 0.0451 * info + 0.5735 * trans + 0.1102 * active + 0.2712 * plat;
        return new JSONObject()
                .element("info", info)
                .element("trans", trans)
                .element("active", active)
                .element("plat", plat)
                .element("credit", credit);
    }
}
