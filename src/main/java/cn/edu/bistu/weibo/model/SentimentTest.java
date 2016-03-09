package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 10/30/15.
 */
public class SentimentTest {
    private int id;
    private String content;
    private float weight;
    private int correct;

    public SentimentTest() {
    }

    public SentimentTest(int id, String content, float weight, int correct) {
        this.id = id;
        this.content = content;
        this.weight = weight;
        this.correct = correct;
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

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "SentimentTest{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", weight=" + weight +
                ", correct=" + correct +
                '}';
    }
}
