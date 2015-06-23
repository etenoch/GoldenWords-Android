package ca.goldenwords.gwandroid.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class ArticleViewFragment extends Fragment {

    View fragmentView;
    Node node;

    public ArticleViewFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.article_view, container, false);
        fragmentView = v;
        EventBus.getDefault().register(this);

        return v;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(Node node){
        this.node = node;
        Date time=new java.util.Date((long)node.revision_timestamp*1000);
        SimpleDateFormat ft = new SimpleDateFormat ("MMM d, y");

        ((TextView) fragmentView.findViewById(R.id.headline)).setText(node.title);
        ((TextView) fragmentView.findViewById(R.id.author)).setText(node.author);
        ((TextView) fragmentView.findViewById(R.id.date)).setText("Published on "+ft.format(time));
        ((TextView) fragmentView.findViewById(R.id.section)).setText(node.article_category);

        ((WebView) fragmentView.findViewById(R.id.webView)).loadData(node.html_content,"text/html","UTF-8");
    }

}
