package ca.goldenwords.gwandroid.http;


import ca.goldenwords.gwandroid.events.StringWrapperEvent;
import de.greenrobot.event.EventBus;

public class GenericFetcher extends AsyncTaskFetcher {

    public GenericFetcher(String stringUrl){
        super(stringUrl);
    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
        method = "GET";
    }

    @Override protected void onPostExecute(String result) {
        EventBus.getDefault().post(new StringWrapperEvent(result));
    }
}
