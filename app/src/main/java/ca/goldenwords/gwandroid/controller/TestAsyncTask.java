package ca.goldenwords.gwandroid.controller;

import android.os.AsyncTask;
import android.widget.TextView;

import ca.goldenwords.gwandroid.MessageEvent;
import de.greenrobot.event.EventBus;

public class TestAsyncTask extends AsyncTask<String, Void, String> {

    @Override protected void onPreExecute() {}

    @Override protected String doInBackground(String... params) {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        return "Executed";
    }

    @Override protected void onPostExecute(String result) {
        EventBus.getDefault().post(new MessageEvent(result));
    }

}