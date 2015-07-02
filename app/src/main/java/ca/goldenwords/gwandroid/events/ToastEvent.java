package ca.goldenwords.gwandroid.events;

public class ToastEvent {
    private String message;

    public ToastEvent(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
