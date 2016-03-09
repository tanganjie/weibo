package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 12/20/15.
 */
public class Attention {
    private int id;
    private String userid;
    private String attentionUserid;

    public Attention(int id, String userid, String attentionUserid) {
        this.id = id;
        this.userid = userid;
        this.attentionUserid = attentionUserid;
    }

    public Attention() {

    }

    public Attention(String userid, String attentionUserid) {
        this.userid = userid;
        this.attentionUserid = attentionUserid;
    }

    @Override
    public String toString() {
        return "Attention{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", attentionUserid='" + attentionUserid + '\'' +
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

    public String getAttentionUserid() {
        return attentionUserid;
    }

    public void setAttentionUserid(String attentionUserid) {
        this.attentionUserid = attentionUserid;
    }
}
