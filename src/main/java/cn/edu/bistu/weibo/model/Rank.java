package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 12/21/15.
 */
public class Rank {
    private String id;
    private String username;
    private int zan;
    private int trans;
    private int comm;
    private int att;
    private int fans;
    private double rank;
    private double valuerank;

    @Override
    public String toString() {
        return "Rank{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", zan=" + zan +
                ", trans=" + trans +
                ", comm=" + comm +
                ", att=" + att +
                ", fans=" + fans +
                ", rank=" + rank +
                ", valuerank=" + valuerank +
                '}';
    }

    public Rank(String id, String username, int zan, int trans, int comm, int att, int fans, double rank, double valuerank) {
        this.id = id;
        this.username = username;
        this.zan = zan;
        this.trans = trans;
        this.comm = comm;
        this.att = att;
        this.fans = fans;
        this.rank = rank;
        this.valuerank = valuerank;
    }

    public Rank() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getAtt() {
        return att;
    }

    public void setAtt(int att) {
        this.att = att;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public double getValuerank() {
        return valuerank;
    }

    public void setValuerank(double valuerank) {
        this.valuerank = valuerank;
    }
}
