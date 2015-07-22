package ca.goldenwords.gwandroid.events;


public class StringWrapperEvent {

    private String data;

    public StringWrapperEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
