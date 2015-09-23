package ca.goldenwords.gwandroid.http;


import org.json.JSONException;

import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Section;
import ca.goldenwords.gwandroid.events.ToastEvent;
import de.greenrobot.event.EventBus;

public class ListFetcher extends AsyncTaskFetcher {

    public enum Type {ISSUE,SECTION}
    private Type type;

    public ListFetcher(String stringUrl,Type type){
        super(stringUrl);
        this.type = type;
    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
        method = "GET";
    }

    @Override protected void onPostExecute(String result) {
        try {
            if (type == Type.ISSUE) {
                Issue issue = Issue.fromJson(result, true);
                if (!isCancelled()) EventBus.getDefault().post(issue);
            } else if (type == Type.SECTION) {
                Section section = Section.fromJson(result, true);
                if (!isCancelled()) EventBus.getDefault().post(section);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ToastEvent("An error has occurred :("));
            // TODO
        }

//        EventBus.getDefault().post(new ToastEvent("No more posts"));
    }
}
