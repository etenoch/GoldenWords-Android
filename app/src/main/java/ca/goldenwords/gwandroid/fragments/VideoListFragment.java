package ca.goldenwords.gwandroid.fragments;


import android.content.Intent;
import android.net.Uri;
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
import ca.goldenwords.gwandroid.adapter.VideoLinkAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.BrowserIntentEvent;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class VideoListFragment extends Fragment{

    private View fragmentView;
    private List<Node> nodes;
    private int currentCount=0;

    private VideoLinkAdapter adp;

    public VideoListFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        fragmentView = inflater.inflate(R.layout.fragment_article_list, container, false);
        DataCache.postSectionToBus("videos");
        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(Section section){
        ProgressBar loading_spinner = (ProgressBar) fragmentView.findViewById(R.id.loading_spinner);
        TextView section_header = (TextView) fragmentView.findViewById(R.id.section_header);
        RecyclerView recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        section_header.setText(section.name);
        recList.setLayoutManager(llm);

        nodes = new ArrayList<>();
        adp = new VideoLinkAdapter(nodes, getActivity());

        // sort Set into List for adapting
        for (Node s : section.nodes) {
            nodes.add(s);
            currentCount++;
        }

        recList.setVisibility(View.VISIBLE);
        recList.setAdapter(adp);
        section_header.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.INVISIBLE);
    }

    public void onEvent(BrowserIntentEvent bie){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bie.getUrl()));
        getActivity().startActivity(browserIntent);
    }

}
