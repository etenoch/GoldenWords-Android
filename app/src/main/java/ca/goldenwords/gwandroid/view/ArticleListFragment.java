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
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class ArticleListFragment extends Fragment {

    View fragmentView;

    public ArticleListFragment() {

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getPersistentView(inflater,container,savedInstanceState);
    }

    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_article_list, container, false);

            String section = getArguments().getString("section");
            new ListFetcher(getString(R.string.baseurl)+"/list/"+section, ListFetcher.Type.SECTION).execute();
        }
        return fragmentView;
    }


    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(Section section){
        ProgressBar loading_spinner = (ProgressBar)fragmentView.findViewById(R.id.loading_spinner);

        TextView section_header = (TextView)fragmentView.findViewById(R.id.section_header);
        section_header.setText(section.name);

        RecyclerView recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
        recList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Node> nodes = new ArrayList<>();

        NodeAdapter adp = new NodeAdapter(nodes,getActivity(),ListFetcher.Type.SECTION);

        // sort Set into List for adapting
        for (Node s : section.nodes) {
            nodes.add(s);
        }

        recList.setVisibility(View.VISIBLE);
        recList.setAdapter(adp);
        section_header.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.INVISIBLE);

    }

}
