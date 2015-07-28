package ca.goldenwords.gwandroid.events;


public class BrowserIntentEvent {

    private String url;

    public BrowserIntentEvent(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
