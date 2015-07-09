package ca.goldenwords.gwandroid.events;

public class ToastEvent {

    private String message;
    private boolean length_long =true;

    public ToastEvent(String message){
        this.message = message;
    }

    public ToastEvent(String message,boolean length_long){
        this.message = message;
        this.length_long = length_long;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLength_long() {
        return length_long;
    }

    public void setLength_long(boolean length_long) {
        this.length_long = length_long;
    }
}
