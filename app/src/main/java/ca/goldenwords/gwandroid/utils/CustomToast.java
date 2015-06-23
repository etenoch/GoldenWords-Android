package ca.goldenwords.gwandroid.utils;

import android.widget.Toast;

public class CustomToast {
    private String message;

    public CustomToast(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
