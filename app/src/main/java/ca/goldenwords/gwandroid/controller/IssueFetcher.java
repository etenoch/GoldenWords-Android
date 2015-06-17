package ca.goldenwords.gwandroid.controller;


import org.json.JSONException;

import ca.goldenwords.gwandroid.model.Issue;
import de.greenrobot.event.EventBus;

public class IssueFetcher extends AsyncTaskFetcher {

    public IssueFetcher(String stringUrl){
        super(stringUrl);
        method = "GET";
    }

    @Override public boolean validateData() {
        return this.stringUrl.contains("api/get/issue");
    }

    @Override protected void onPostExecute(String result) {
        try{
            Issue issue = Issue.fromJson(result);
            EventBus.getDefault().post(issue);
        }catch(JSONException e){
            e.printStackTrace();
            // TODO don't be a lazy fuck and handle this error
        }
    }

}
