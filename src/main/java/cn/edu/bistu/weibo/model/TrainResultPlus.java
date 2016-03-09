package cn.edu.bistu.weibo.model;

/**
 * Created by tanjie on 10/26/15.
 */
public class TrainResultPlus {
    private int id;
    private int type;
    private String word;
    private int weight;
    private double x2;

    public TrainResultPlus() {
    }

    public TrainResultPlus(int type, String word, int weight, double x2) {
        this.type = type;
        this.word = word;
        this.weight = weight;
        this.x2 = x2;
    }

    public TrainResultPlus(int id, int type, String word, int weight, double x2) {
        this.id = id;
        this.type = type;
        this.word = word;
        this.weight = weight;
        this.x2 = x2;
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

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    @Override
    public String toString() {
        return "TrainResultPlus{" +
                "id=" + id +
                ", type=" + type +
                ", word='" + word + '\'' +
                ", weight=" + weight +
                ", x2=" + x2 +
                '}';
    }
}
