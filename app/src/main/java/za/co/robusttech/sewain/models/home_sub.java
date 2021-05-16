package za.co.robusttech.sewain.models;

public class home_sub {

    private String topic;
    private int topic_img;

    public home_sub(){
    }

    public home_sub(String topic, int topic_img) {
        this.topic = topic;
        this.topic_img = topic_img;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTopic_img() {
        return topic_img;
    }

    public void setTopic_img(int topic_img) {
        this.topic_img = topic_img;
    }
}
