package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 10/16/15.
 */
public class Sentiment {
    private int id;
    private String content;
    private float weight;

    public Sentiment() {
    }

    public Sentiment(int id, String content, float weight) {
        this.id = id;
        this.content = content;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Sentiment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", weight=" + weight +
                '}';
    }
}
