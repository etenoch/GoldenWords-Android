package ca.goldenwords.gwandroid.http;


import android.widget.Toast;

import org.json.JSONException;

import ca.goldenwords.gwandroid.data.DataSource;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Section;
import ca.goldenwords.gwandroid.utils.CustomToast;
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
        try{
            if(type == Type.ISSUE){
                Issue issue = Issue.fromJson(result,true);
                EventBus.getDefault().post(issue);
            }else if (type==Type.SECTION){
                Section section = Section.fromJson(result,true);
                EventBus.getDefault().post(section);
            }
        }catch(JSONException e){
            e.printStackTrace();
            EventBus.getDefault().post(new CustomToast("Oops. The dev fucked up :("));
            // TODO don't be a lazy fuck and handle this error
        }
    }

}
