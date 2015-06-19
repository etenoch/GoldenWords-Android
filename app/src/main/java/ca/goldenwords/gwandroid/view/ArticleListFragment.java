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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.controller.IssueFetcher;
import ca.goldenwords.gwandroid.controller.SectionFetcher;
import ca.goldenwords.gwandroid.model.Article;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class ArticleListFragment extends Fragment {

    View fragmentView;

    public ArticleListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_list, container, false);
        fragmentView = v;
        String section = getArguments().getString("section");

        new SectionFetcher("http://goldenwords.ca/api/get/list/"+section).execute();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(Section section){
        ProgressBar loading_spinner = (ProgressBar)fragmentView.findViewById(R.id.loading_spinner);

        TextView section_header = (TextView)fragmentView.findViewById(R.id.section_header);
        section_header.setText(section.name);

        RecyclerView recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recList.setLayoutManager(llm);

        List<Node> nodes = new ArrayList<>();

        NodeAdapter adp = new NodeAdapter(nodes,getActivity());

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
