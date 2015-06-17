package ca.goldenwords.gwandroid.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.controller.IssueFetcher;
import ca.goldenwords.gwandroid.controller.SectionFetcher;
import ca.goldenwords.gwandroid.model.Article;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class ArticleListFragment extends Fragment {

    public ArticleListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_list, container, false);
        new SectionFetcher("http://goldenwords.ca/api/get/list/news").execute();
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

    public void onEvent(Section section) {
        String title = section.nodes.iterator().next().title;
        Toast.makeText(getActivity(),title,Toast.LENGTH_LONG).show();
    }

}
