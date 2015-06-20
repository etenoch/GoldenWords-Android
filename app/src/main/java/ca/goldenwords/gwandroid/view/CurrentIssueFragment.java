package ca.goldenwords.gwandroid.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.http.IssueFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import de.greenrobot.event.EventBus;

public class CurrentIssueFragment extends Fragment {

    View fragmentView;

    public CurrentIssueFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_issue, container, false);
        fragmentView = v;

        new IssueFetcher(getString(R.string.baseurl)+"/issue").execute();
        return v;
    }

    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(Issue currentIssue){
        ProgressBar loading_spinner = (ProgressBar)fragmentView.findViewById(R.id.loading_spinner);

        TextView volume_issue_header = (TextView)fragmentView.findViewById(R.id.volume_issue_header);
        volume_issue_header.setText("Volume " + currentIssue.volume + " - Issue " + currentIssue.issue);

        RecyclerView recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recList.setLayoutManager(llm);

        List<Node> nodes = new ArrayList<>();

        NodeAdapter adp = new NodeAdapter(nodes,getActivity());

        // sort HashMap into List for adapting
        for (Node s : currentIssue.nodes) {
            if(s.cover_image==1) nodes.add(0, s);
            else nodes.add(s);
        }

        recList.setVisibility(View.VISIBLE);
        recList.setAdapter(adp);
        volume_issue_header.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.INVISIBLE);

    }


}
