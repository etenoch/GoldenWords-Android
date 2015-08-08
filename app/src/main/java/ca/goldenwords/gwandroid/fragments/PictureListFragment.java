package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.GridViewAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ImageDownloadedEvent;
import ca.goldenwords.gwandroid.model.ImageItem;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class PictureListFragment extends Fragment{

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private View fragmentView;

    private HashMap<String,Node> nodeTracker;
    private ArrayList<ImageItem> list;
    private Section section;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        fragmentView = inflater.inflate(R.layout.fragment_picture_grid, container, false);

        gridView = (GridView) fragmentView.findViewById(R.id.gridView);
        DataCache.downloaderTasks.add(DataCache.postSectionToBus("pictures"));

        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        DataCache.clearDownloaders();
        super.onStop();
    }

    public void onEvent(Section section){
        this.section = section;
        nodeTracker = new HashMap<>();
        list = new ArrayList<>();

        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, list);
        gridView.setAdapter(gridAdapter);

        for (Node s : section.nodes) {
            if(s.image_url!=null){
                nodeTracker.put(s.image_url,s);
                DataCache.downloaderTasks.add(DataCache.postImageToBus(null, s.image_url));
            }
        }

        fragmentView.findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);
    }

    public void onEvent(ImageDownloadedEvent image){
        ImageItem ii = new ImageItem(image.getImage(),nodeTracker.get(image.getUrl()).title);
        list.add(ii);
        gridAdapter.notifyDataSetChanged();

//        if(list.size()>=section.nodes.size()){
//            fragmentView.findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);
//
//        }
    }


}
