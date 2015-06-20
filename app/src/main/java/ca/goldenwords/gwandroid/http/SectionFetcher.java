package ca.goldenwords.gwandroid.http;


import org.json.JSONException;

import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class SectionFetcher extends AsyncTaskFetcher {

    public SectionFetcher(String stringUrl){
        super(stringUrl);
        method = "GET";
    }

    @Override public boolean validateData() {
        return this.stringUrl.contains("api/get/list");
    }

    @Override protected void onPostExecute(String result) {
        try{
            Section section = Section.fromJson(result);
            EventBus.getDefault().post(section);
        }catch(JSONException e){
            e.printStackTrace();
            // TODO don't be a lazy fuck and handle this error
        }
    }

}
