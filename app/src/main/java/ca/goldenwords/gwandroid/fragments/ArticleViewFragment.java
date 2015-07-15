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

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.goldenwords.gwandroid.MainActivity;
import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ImageDownloadedEvent;
import ca.goldenwords.gwandroid.events.ToastEvent;
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
            @Override public void onClick(View v) {
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
            iv.setImageResource(R.drawable.ic_placeholder);
            DataCache.postImageToBus(iv, node.image_url);
        }else iv.setVisibility(View.GONE);
        ((WebView) fragmentView.findViewById(R.id.webView)).loadDataWithBaseURL(null, node.html_content, "text/html", "UTF-8", null);

        Date time=new java.util.Date((long)node.revision_timestamp*1000);
        SimpleDateFormat ft = new SimpleDateFormat ("MMM d, y");

        ((TextView) fragmentView.findViewById(R.id.headline)).setText(node.title);
        ((TextView) fragmentView.findViewById(R.id.author)).setText(node.author);
        ((TextView) fragmentView.findViewById(R.id.date)).setText("Published on " + ft.format(time));
        ((TextView) fragmentView.findViewById(R.id.section)).setText(node.article_category);
    }

    public void onEvent(ImageDownloadedEvent e){
        ImageView iv = e.getImageView();
        iv.setImageBitmap(e.getImage());
    }

}
