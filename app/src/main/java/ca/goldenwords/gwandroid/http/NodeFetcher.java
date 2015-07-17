package ca.goldenwords.gwandroid.http;


import org.json.JSONException;

import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.events.ToastEvent;
import de.greenrobot.event.EventBus;

public class NodeFetcher extends AsyncTaskFetcher {

    public NodeFetcher(String stringUrl){
        super(stringUrl);
    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
        method = "GET";
    }

    @Override protected void onPostExecute(String result) {
        try{
            Node node = Node.fromJson(result);
            if(!isCancelled()) EventBus.getDefault().post(node);
        }catch(JSONException e){
            e.printStackTrace();
            EventBus.getDefault().post(new ToastEvent("Oops. The dev fucked up :("));
            // TODO don't be a lazy fuck and handle this error
        }
    }

}
