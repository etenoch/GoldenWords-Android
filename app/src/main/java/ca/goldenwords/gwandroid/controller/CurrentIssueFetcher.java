package ca.goldenwords.gwandroid.controller;


import android.os.AsyncTask;
import ca.goldenwords.gwandroid.model.Issue;
import de.greenrobot.event.EventBus;

public class CurrentIssueFetcher extends AsyncTask<String, Void, String> {

    @Override protected void onPreExecute() {

    }

    @Override protected String doInBackground(String... params) {
        // do http request to api here
        return "current issue text";
    }

    @Override protected void onPostExecute(String result) {
        // parse json and make Issue object
        EventBus.getDefault().post(new Issue(result));
    }

}
