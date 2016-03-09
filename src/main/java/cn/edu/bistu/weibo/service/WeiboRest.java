package cn.edu.bistu.weibo.service;

import cn.edu.bistu.common.nlp.test.Predict;
import cn.edu.bistu.common.usercredit.UserCreditCalculate;
import cn.edu.bistu.common.weibocredit.PredictWeibo;
import cn.edu.bistu.common.weibocredit.WeiboCreditCalculate;
import cn.edu.bistu.weibo.dao.UsersMapper;
import cn.edu.bistu.weibo.dao.WeibosMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Users;
import cn.edu.bistu.weibo.model.Weibos;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by tanjie on 12/22/15.
 */
@Path("/Weibos")
public class WeiboRest {
    protected static Logger logger = Logger.getLogger(WeiboRest.class);

    @POST
    @Path("/queryUserWeibos")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryUserWeibos(@FormParam("field") String field,
                                      @FormParam("userid") String userid,
                                      @FormParam("asc") String asc,
                                      @FormParam("pn") int pn,
                                      @FormParam("cp") int cp,
                                      @FormParam("search") String search) {
        SqlSession session = SessionFactory.getFactory().openSession();
        WeibosMapper weibosMapper = session.getMapper(WeibosMapper.class);
        search = "%" + search + "%";

        List<Weibos> weiboses = weibosMapper.queryWeibosByUserId(userid, field, asc, pn, cp, search);
        int count = weibosMapper.queryWeibosSize(userid, search);

        session.close();
        JSONArray array = JSONArray.fromObject(weiboses);

        for(int i = 0; i < array.size(); i++) {
            array.getJSONObject(i).element("linenumber", pn + i + 1);
        }
        return new JSONObject().element("total", count).element("rows", array);
    }


    @POST
    @Path("/queryUser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryUserInfo(@FormParam("userid") String userid){
        SqlSession session = SessionFactory.getFactory().openSession();
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        Users users = usersMapper.queryUserById(userid);
        session.close();
        if(users == null) {
            return  null;
        }
        return JSONObject.fromObject(users);
    }

    @POST
    @Path("/queryWeibo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryWeiboInfo(@FormParam("weiboid") int weiboid) {
        SqlSession session = SessionFactory.getFactory().openSession();
        WeibosMapper weibosMapper = session.getMapper(WeibosMapper.class);
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);

        Weibos w = weibosMapper.queryById(weiboid);
        Users u = usersMapper.queryUserById(w.getUserid());

        session.close();

        return new JSONObject().element("userinfo", JSONObject.fromObject(u)).element("weiboinfo", JSONObject.fromObject(w));
    }

    @POST
    @Path("/handleweibo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject handleWeibo(@FormParam("weiboid") int weiboid) {
        SqlSession session = SessionFactory.getFactory().openSession();
        WeibosMapper weibosMapper = session.getMapper(WeibosMapper.class);
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        Weibos w = weibosMapper.queryById(weiboid);
        if(w==null){
            return null;
        }

        Users user = usersMapper.queryUserById(w.getUserid());
        session.close();

        double info = UserCreditCalculate.infoComlete(user);
        double trans = UserCreditCalculate.rankCal(user);
        double active = UserCreditCalculate.activeCal(user);
        double plat = UserCreditCalculate.platFormCal(user);
        double credit = 0.0451 * info + 0.5735 * trans + 0.1102 * active + 0.2712 * plat;

        try {
            WeiboCreditCalculate weiboCreditCalculate = new WeiboCreditCalculate(w.getUserid(), w.getContent());
            int r = PredictWeibo.predict(w.getUserid(), w.getContent());
            logger.info("rumour:" + r);
            JSONObject json = new JSONObject();
            json.element("seg", BaseAnalysis.parse(w.getContent()).toString()).element("info", weiboCreditCalculate.extractInfo());
            json.element("rumour", r);
            json.element("credit", PredictWeibo.calCredit(r, credit));
            logger.info(json);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
