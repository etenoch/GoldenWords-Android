package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ca.goldenwords.gwandroid.R;


public class IssueArchiveFragment extends Fragment{

    public IssueArchiveFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_contact_us, container, false);

        ((TextView) fragmentView.findViewById(R.id.textView)).setText("Issue Archive");

        WebView wv = (WebView) fragmentView.findViewById(R.id.aboutUsWebView);
        wv.loadUrl("http://goldenwords.ca/archive/");

        ProgressBar loading_spinner = (ProgressBar) fragmentView.findViewById(R.id.loading_spinner);
        loading_spinner.setVisibility(View.GONE);

        return fragmentView;
    }

}
