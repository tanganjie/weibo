package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 10/16/15.
 */
public class TrainResult {
    private int id;
    private int type;
    private String word;
    private int weight;

    public TrainResult() {
    }

    public TrainResult(int type, String word, int weight) {
        this.type = type;
        this.word = word;
        this.weight = weight;
    }

    public TrainResult(int id, int type, String word, int weight) {
        this.id = id;
        this.type = type;
        this.word = word;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "TrainResult{" +
                "id=" + id +
                ", type=" + type +
                ", word='" + word + '\'' +
                ", weight=" + weight +
                '}';
    }
}
