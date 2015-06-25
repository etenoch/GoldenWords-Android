package ca.goldenwords.gwandroid.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.goldenwords.gwandroid.MainActivity;
import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import ca.goldenwords.gwandroid.utils.BaseBackPressedListener;
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

        ((MainActivity) getActivity()).getMDrawerToggle().setDrawerIndicatorEnabled(false);
        ((MainActivity) getActivity()).getMDrawerToggle().setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
//        ((MainActivity)getActivity()).getToolbar().setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
//        ((MainActivity)getActivity()).getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "menu click", Toast.LENGTH_SHORT).show();
//            }
//        });

        ((MainActivity)getActivity()).getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return v;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override public void onDetach() {
        super.onDetach();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
        ((MainActivity)getActivity()).getMDrawerToggle().setDrawerIndicatorEnabled(true);
        ((MainActivity)getActivity()).getMDrawerToggle().syncState();
        ((MainActivity)getActivity()).getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

//    @Override public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(getActivity(),"menu click",Toast.LENGTH_SHORT).show();
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Toast.makeText(getActivity(),"home click",Toast.LENGTH_SHORT).show();
//                getActivity().getFragmentManager().popBackStack();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    public void onEvent(Node node){
        this.node = node;
        Date time=new java.util.Date((long)node.revision_timestamp*1000);
        SimpleDateFormat ft = new SimpleDateFormat ("MMM d, y");

        ((TextView) fragmentView.findViewById(R.id.headline)).setText(node.title);
        ((TextView) fragmentView.findViewById(R.id.author)).setText(node.author);
        ((TextView) fragmentView.findViewById(R.id.date)).setText("Published on "+ft.format(time));
        ((TextView) fragmentView.findViewById(R.id.section)).setText(node.article_category);

        ((WebView) fragmentView.findViewById(R.id.webView)).loadDataWithBaseURL(null, node.html_content,"text/html","UTF-8",null);
    }

}
