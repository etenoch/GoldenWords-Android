package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ToastEvent;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class ArticleListFragment extends Fragment {

    private View fragmentView;
    private int currentCount=0;
    private boolean dataLoaded = false;
    private String section;

    // endless recyler view stuff
    private boolean okToFetchMore = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private ProgressBar loading_spinner;
    private ProgressBar loading_bar;
    private TextView section_header;
    private RecyclerView recList;
    private LinearLayoutManager llm;
    private List<Node> nodes;
    private NodeAdapter adp;

    Button loadMore;
    DisplayMetrics displayMetrics;
    int px;

    public ArticleListFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return getPersistentView(inflater,container,savedInstanceState);
    }

    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_article_list, container, false);
            section = getArguments().getString("section");

            loading_spinner = (ProgressBar) fragmentView.findViewById(R.id.loading_spinner);
            loading_bar = (ProgressBar) fragmentView.findViewById(R.id.loading_bar);

            displayMetrics = getActivity().getResources().getDisplayMetrics();
            px = Math.round(48 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

            loadMore = (Button)fragmentView.findViewById(R.id.loadMore);
            loadMore.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    loading_bar.setVisibility(View.VISIBLE);
                    loadMore.animate().translationY(px);
                    EventBus.getDefault().post(new ToastEvent("Loading more items",false));
                    okToFetchMore = false;
                    DataCache.downloaderTasks.add(DataCache.postSectionToBus(section, currentCount + 1));
                }
            });

            DataCache.downloaderTasks.add(DataCache.postSectionToBus(section));
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
    public void onEvent(Section section){
        final String sectionShortName = this.section;
        if(!dataLoaded) {
            section_header = (TextView) fragmentView.findViewById(R.id.section_header);
            recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
            llm = new LinearLayoutManager(getActivity());

            section_header.setText(section.name);
            recList.setLayoutManager(llm);

            recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();
                    if(okToFetchMore){
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) loadMore.animate().translationY(0);
                        else loadMore.animate().translationY(px);
                    }

                }
            });


            nodes = new ArrayList<>();
            adp = new NodeAdapter(nodes, getActivity(), ListFetcher.Type.SECTION,this);

            // sort Set into List for adapting
            for (Node s : section.nodes) {
                nodes.add(s);
                currentCount++;
            }

            recList.setVisibility(View.VISIBLE);
            recList.setAdapter(adp);
            section_header.setVisibility(View.VISIBLE);
            loading_spinner.setVisibility(View.INVISIBLE);
            dataLoaded=true;
        }else{ // load more
            for (Node s : section.nodes) {
                if(!nodes.contains(s)) {
                    nodes.add(s);
                    currentCount++;
                }
            }
            adp.notifyDataSetChanged();
            loading_bar.setVisibility(View.INVISIBLE);
            okToFetchMore = true;
        }
    }

}
