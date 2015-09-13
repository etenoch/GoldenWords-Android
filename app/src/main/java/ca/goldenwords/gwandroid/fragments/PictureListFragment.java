package ca.goldenwords.gwandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.GridViewAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ImageDownloadedEvent;
import ca.goldenwords.gwandroid.events.ToastEvent;
import ca.goldenwords.gwandroid.model.ImageItem;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class PictureListFragment extends Fragment{

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private ViewPager viewPager;
    private ImagePagerAdapter imagePagerAdapter;
    private View viewPagerWrapper;

    private View fragmentView;

    private HashMap<String,Node> nodeTracker  = new HashMap<>();
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private Section section;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        fragmentView = inflater.inflate(R.layout.fragment_picture_grid, container, false);

        gridView = (GridView) fragmentView.findViewById(R.id.gridView);
        DataCache.downloaderTasks.add(DataCache.postSectionToBus("pictures"));

        viewPagerWrapper = fragmentView.findViewById(R.id.view_pager_wrapper);
        viewPager = (ViewPager) fragmentView.findViewById(R.id.view_pager);
        imagePagerAdapter = new ImagePagerAdapter();
        viewPager.setAdapter(imagePagerAdapter);

        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        DataCache.clearDownloaders();
        super.onStop();
    }

    public void onEvent(Section section){
        this.section = section;
//        nodeTracker = new HashMap<>()

        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);

        for (Node s : section.nodes) {
            if(s.image_url!=null){
                nodeTracker.put(s.image_url, s);
                DataCache.downloaderTasks.add(DataCache.postImageToBus(null, s.image_url));
            }
        }

        fragmentView.findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                viewPagerWrapper.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new ToastEvent(imageItems.get(position).toString()));
            }
        });

        viewPagerWrapper.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                viewPagerWrapper.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new ToastEvent("tapped"));
            }
        });

    }

    public void onEvent(ImageDownloadedEvent image){
        ImageItem ii = new ImageItem(image.getImage(),nodeTracker.get(image.getUrl()).title);
        imageItems.add(ii);
        imagePagerAdapter.notifyDataSetChanged();
        gridAdapter.notifyDataSetChanged();

    }


    private class ImagePagerAdapter extends PagerAdapter {

        @Override public int getCount() {
            return imageItems.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            Context context = getActivity();
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageBitmap(imageItems.get(position).getImage());
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }



}
