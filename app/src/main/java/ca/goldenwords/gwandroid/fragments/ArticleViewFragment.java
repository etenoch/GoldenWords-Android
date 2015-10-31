package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.goldenwords.gwandroid.MainActivity;
import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.model.Node;
import de.greenrobot.event.EventBus;

public class ArticleViewFragment extends Fragment {

    private View fragmentView;
    private Node node;

    public ArticleViewFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.article_view, container, false);
        fragmentView = v;
        EventBus.getDefault().register(this);

        // change action bar. back button
        MainActivity ac = (MainActivity)getActivity();
        ac.getMDrawerToggle().setDrawerIndicatorEnabled(false);
        ac.getMDrawerToggle().setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        ac.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return v;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        ((MainActivity)getActivity()).setCurrentShareUrl(getString(R.string.siteurl), "Golden Words");
        super.onStop();
    }

    // change action bar back
    @Override public void onDetach() {
        super.onDetach();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
        MainActivity ac = (MainActivity)getActivity();
        ac.getMDrawerToggle().setDrawerIndicatorEnabled(true);
        ac.getMDrawerToggle().syncState();
        ac.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    // event bus events
    public void onEvent(Node node){
        this.node = node;
        ImageView iv = (ImageView)fragmentView.findViewById(R.id.articleImage);
        if(node.image_url!=null){
            Picasso.with(getActivity()).load(node.image_url).placeholder(R.drawable.ic_placeholder).into(iv);
        }else iv.setVisibility(View.GONE);

        ((WebView) fragmentView.findViewById(R.id.webView)).loadDataWithBaseURL(null, node.html_content, "text/html", "UTF-8", null);

        Date time=new java.util.Date((long)node.revision_timestamp*1000);
        SimpleDateFormat ft = new SimpleDateFormat ("MMM d, y");

        ((TextView) fragmentView.findViewById(R.id.headline)).setText(node.title);
        ((TextView) fragmentView.findViewById(R.id.author)).setText(node.author);
        ((TextView) fragmentView.findViewById(R.id.date)).setText("Published on " + ft.format(time));
        ((TextView) fragmentView.findViewById(R.id.section)).setText(node.article_category);

        // display tags
        String tagString = "";
        if(node.tags.size()>0) {
            tagString = "Tags: ";
            for (String tag:node.tags){
                tagString+=tag+", ";
            }
            tagString = tagString.substring(0, tagString.length()-2); // get rid of comma
            ((TextView) fragmentView.findViewById(R.id.tags)).setText(tagString);
        }

        // volume issue
        String volumeIssue = node.volume+" - "+node.issue;
        ((TextView) fragmentView.findViewById(R.id.volume_issue)).setText(volumeIssue);

        ((MainActivity)getActivity()).setCurrentShareUrl(getString(R.string.siteurl) + "/node/" + node.nid, node.title);

    }

}
