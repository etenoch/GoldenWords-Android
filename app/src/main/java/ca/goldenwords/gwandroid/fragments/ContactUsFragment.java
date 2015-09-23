package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.StringWrapperEvent;
import ca.goldenwords.gwandroid.events.ToastEvent;
import ca.goldenwords.gwandroid.http.GenericFetcher;
import de.greenrobot.event.EventBus;

public class ContactUsFragment extends Fragment{

    private View fragmentView;
    private boolean dataLoaded = false;

    public ContactUsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return getPersistentView(inflater, container, savedInstanceState);
    }

    private View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_contact_us, container, false);
            new GenericFetcher(getResources().getString(R.string.contactus)).execute();
        }
        return fragmentView;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        DataCache.clearDownloaders();
        if (!dataLoaded) fragmentView = null;
        dataLoaded = false;
        super.onStop();
    }

    public void onEvent(StringWrapperEvent e) {
        if(!dataLoaded) {
            try{
                WebView webview = (WebView)fragmentView.findViewById(R.id.aboutUsWebView);
                ProgressBar loading_spinner = (ProgressBar) fragmentView.findViewById(R.id.loading_spinner);
                loading_spinner.setVisibility(View.GONE);

                JSONObject jo = new JSONObject(e.getData());
                String html = jo.getJSONObject("body").getString("value");
                webview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                dataLoaded=true;
            } catch (JSONException exception) {
                exception.printStackTrace();
                EventBus.getDefault().post(new ToastEvent("An error has occurred :("));
                // TODO god dammit Enoch, don't be a lazy fuck and handle this error properly
            }
        }
    }

}
