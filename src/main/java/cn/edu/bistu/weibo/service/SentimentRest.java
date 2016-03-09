package cn.edu.bistu.weibo.service;


import cn.edu.bistu.common.nlp.sentiment.analysis.bayes.Analysis;
import cn.edu.bistu.weibo.dao.SentimentTestMapper;
import cn.edu.bistu.weibo.dao.SentimentTrainMapper;
import cn.edu.bistu.weibo.dbutil.SessionFactory;
import cn.edu.bistu.weibo.model.Sentiment;
import cn.edu.bistu.weibo.model.SentimentTest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by tanjie on 10/16/15.
 */
@Path("/Sentiment")
public class SentimentRest {
    protected static Logger log = Logger.getLogger(SentimentRest.class);

    /**
     * 获取所有数据
     * @return
     */
    @GET
    @Path("/queryTrainData")
    @Produces("application/json;charset=UTF-8")
    public JSONArray queryTrainData() {
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainMapper mapper = session.getMapper(SentimentTrainMapper.class);
        List<Sentiment> list = mapper.queryAll();

        //JSONObject json = new JSONObject();
        //json.element("data", JSONArray)

        return JSONArray.fromObject(list);
    }

    /**
     * 服务端分页处理
     * @param field
     * @param asc
     * @param pn
     * @param cp
     * @return
     */
    @POST
    @Path("/queryTrainDataInPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryTrainData(@FormParam("field") String field,
                                     @FormParam("asc") String asc,
                                     @FormParam("pn") int pn,
                                     @FormParam("cp") int cp,
                                     @FormParam("search") String search) {
        //log.info("field:" + field + ",asc:" + asc + ",pn:" + pn + ",cp:" + cp);
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTrainMapper mapper = session.getMapper(SentimentTrainMapper.class);
        //int start = (pn - 1) * cp;
        search = "%" + search + "%";
        List<Sentiment> list = mapper.queryInPage(field, asc, pn, cp, search);
        int total = mapper.querySearchSize(search);
        session.commit();
        session.close();

        JSONArray array = JSONArray.fromObject(list);
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            object.element("linenumber", pn + i + 1);
            double f = (double) object.get("weight");
            if(f == 0)
                object.element("weight", "负面");
            else if(f == 1)
                object.element("weight", "正面");
            else
                object.element("weight", "中立");
        }

        JSONObject json = new JSONObject();
        json.element("total", total)
                .element("rows", array);
        return json;
    }

    @POST
    @Path("/queryTestDataInPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject queryTestData(@FormParam("field") String field,
                                    @FormParam("asc") String asc,
                                    @FormParam("pn") int pn,
                                    @FormParam("cp") int cp,
                                    @FormParam("search") String search){
        SqlSession session = SessionFactory.getFactory().openSession();
        SentimentTestMapper mapper = session.getMapper(SentimentTestMapper.class);
        search = "%" + search + "%";
        List<SentimentTest> list = mapper.queryInPage(field, asc, pn, cp, search);
        int total = mapper.queryPageSize(search);
        session.commit();
        session.close();

        JSONArray array = JSONArray.fromObject(list);
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            object.element("linenumber", pn + i + 1);
            double f = (double) object.get("weight");
            int c = (int) object.get("correct");
            if(f == 0)
                object.element("weight", "负面");
            else if(f == 1)
                object.element("weight", "正面");
            else
                object.element("weight", "中立");
            if(c == 0)
                object.element("correct", "负面");
            else if(c == 2)
                object.element("correct", "正面");
            else
                object.element("correct", "中立");
        }

        JSONObject json = new JSONObject();
        json.element("total", total)
                .element("rows", array);
        return json;
    }

    @POST
    @Path("/analysis")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json;charset=UTF-8")
    public JSONObject analysis(@FormParam("weibo") String weibo, @FormParam("method") String method) {
        JSONObject json = new JSONObject();
        json.element("weibo", weibo);
        json.element("method", method);
        try {
            if(method.equals("Bayes")) {
                Analysis analysis = new Analysis(0);
                int flag = analysis.classfyWithNeg(weibo);
                json.element("status", "ok");
                if(flag == 0) {
                    json.element("sentiment", "负面");
                } else if(flag == 2) {
                    json.element("sentiment", "正面");
                } else {
                    json.element("sentiment", "中立");
                }
            } else if(method.equals("SVM")){

            } else {
                json.element("status", "error");
            }
        } catch (IOException e) {
            log.error(e);
            json.element("status", "error");
        }
        return json;
    }
}
