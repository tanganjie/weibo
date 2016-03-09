package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 12/18/15.
 */
public class Users {
    private String id;
    private String name;
    private String sex;
    private String area;
    private String birthday;
    private String summary;
    private String tag;
    private String blog;
    private String qq;
    private String education;
    private String work;
    private String official;
    private int zan;
    private int trans;
    private int comm;
    private int att;
    private int fans;
    private int weibos;
    private int pages;

    public Users() {
    }

    public Users(String id, String name, String sex, String area, String birthday, String summary, String tag, String blog, String qq, String education, String work, String official, int zan, int trans, int comm, int att, int fans, int weibos, int pages) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.area = area;
        this.birthday = birthday;
        this.summary = summary;
        this.tag = tag;
        this.blog = blog;
        this.qq = qq;
        this.education = education;
        this.work = work;
        this.official = official;
        this.zan = zan;
        this.trans = trans;
        this.comm = comm;
        this.att = att;
        this.fans = fans;
        this.weibos = weibos;
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", area='" + area + '\'' +
                ", birthday='" + birthday + '\'' +
                ", summary='" + summary + '\'' +
                ", tag='" + tag + '\'' +
                ", blog='" + blog + '\'' +
                ", qq='" + qq + '\'' +
                ", education='" + education + '\'' +
                ", work='" + work + '\'' +
                ", official='" + official + '\'' +
                ", zan=" + zan +
                ", trans=" + trans +
                ", comm=" + comm +
                ", att=" + att +
                ", fans=" + fans +
                ", weibos=" + weibos +
                ", pages=" + pages +
                '}';
    }

    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
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

    public int getWeibos() {
        return weibos;
    }

    public void setWeibos(int weibos) {
        this.weibos = weibos;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
