package ca.goldenwords.gwandroid.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ImageDownloadedEvent;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import de.greenrobot.event.EventBus;

public class CurrentIssueFragment extends Fragment {

    private View fragmentView;
    private boolean dataLoaded = false;

    public CurrentIssueFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return getPersistentView(inflater,container,savedInstanceState);
    }

    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_current_issue, container, false);
            DataCache.downloaderTasks.add(DataCache.postIssueToBus());
        }
        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        DataCache.clearDownloaders();
        if(!dataLoaded) fragmentView = null;
        dataLoaded = false;
        super.onStop();
    }

    // event bus handler
    // data has loaded
    public void onEvent(Issue currentIssue){
        if(!dataLoaded) {
            ProgressBar loading_spinner = (ProgressBar)fragmentView.findViewById(R.id.loading_spinner);

            TextView volume_issue_header = (TextView)fragmentView.findViewById(R.id.volume_issue_header);
            volume_issue_header.setText("Volume " + currentIssue.volume_id + " - Issue " + currentIssue.issue_id);

            RecyclerView recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
            recList.setLayoutManager(new LinearLayoutManager(getActivity()));

            List<Node> nodes = new ArrayList<>();

            NodeAdapter adp = new NodeAdapter(nodes,getActivity(),ListFetcher.Type.ISSUE);

            // sort HashMap into List for adapting
            for (Node s : currentIssue.nodes) {
                if(s.cover_image==1) nodes.add(0, s); // put cover image first
                else nodes.add(s);
            }

            recList.setVisibility(View.VISIBLE);
            recList.setAdapter(adp);
            volume_issue_header.setVisibility(View.VISIBLE);
            loading_spinner.setVisibility(View.INVISIBLE);
            dataLoaded=true;
        }

    }

    public void onEvent(ImageDownloadedEvent e){
        ImageView iv = e.getImageView();
        iv.setImageBitmap(e.getImage());
    }


}
