package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 12/21/15.
 */
public class Weibos {
    private int id;
    private String userid;
    private String content;
    private int zan;
    private int trans;
    private int comm;

    public Weibos() {
    }

    public Weibos(int id, String userid, String content, int zan, int trans, int comm) {
        this.id = id;
        this.userid = userid;
        this.content = content;
        this.zan = zan;
        this.trans = trans;
        this.comm = comm;
    }

    public Weibos(String userid, String content, int zan, int trans, int comm) {
        this.userid = userid;
        this.content = content;
        this.zan = zan;
        this.trans = trans;
        this.comm = comm;
    }

    @Override
    public String toString() {
        return "Weibos{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", content='" + content + '\'' +
                ", zan=" + zan +
                ", trans=" + trans +
                ", comm=" + comm +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getTrans() {
        return trans;
    }

    public void setTrans(int trans) {
        this.trans = trans;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }
}
