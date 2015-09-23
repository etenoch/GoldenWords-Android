package ca.goldenwords.gwandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.HashMap;

import ca.goldenwords.gwandroid.MainActivity;
import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.GridViewAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ImageDownloadedEvent;
import ca.goldenwords.gwandroid.events.ToastEvent;
import ca.goldenwords.gwandroid.model.ImageItem;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import de.greenrobot.event.EventBus;

public class PictureGridFragment extends Fragment{

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private ViewPager viewPager;
    private ImagePagerAdapter imagePagerAdapter;
    private View viewPagerWrapper;

    private View fragmentView;

    private HashMap<String,Node> nodeTracker  = new HashMap<>();
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private ArrayList<Node> nodeList = new ArrayList<>();
    private Section section;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        fragmentView = inflater.inflate(R.layout.fragment_picture_grid, container, false);

        gridView = (GridView) fragmentView.findViewById(R.id.gridView);

        viewPagerWrapper = fragmentView.findViewById(R.id.view_pager_wrapper);
        viewPager = (ViewPager) fragmentView.findViewById(R.id.view_pager);
        imagePagerAdapter = new ImagePagerAdapter();
        viewPager.setAdapter(imagePagerAdapter);

        DataCache.downloaderTasks.add(DataCache.postSectionToBus("pictures"));

        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        DataCache.clearDownloaders();
        super.onStop();
    }

    public void onEvent(Section section){ // setup everything
        this.section = section;

        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);

        for (Node s : section.nodes) {
            if(s.image_url!=null){
                nodeTracker.put(s.image_url, s);
                nodeList.add(s);
                DataCache.downloaderTasks.add(DataCache.postImageToBus(null, s.image_url));
            }
        }

        fragmentView.findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);

        fragmentView.setFocusableInTouchMode(true);
        fragmentView.requestFocus();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                viewPager.setCurrentItem(position, false);
                fadeInPagerWrapper();

                // change action bar. back button
                final MainActivity ac = (MainActivity) getActivity();
                final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

                ac.getMDrawerToggle().setDrawerIndicatorEnabled(false);
                ac.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                ac.getMDrawerToggle().setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fadeOutPagerWrapper();

                        // reset action bar
                        if (actionBar != null) {
                            actionBar.setDisplayHomeAsUpEnabled(false);
                            actionBar.setHomeButtonEnabled(false);
                        }
                        ac.getMDrawerToggle().setDrawerIndicatorEnabled(true);
                        ac.getMDrawerToggle().syncState();
                        ac.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                });
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeButtonEnabled(true);
                }

                // overide system back button
                fragmentView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            fadeOutPagerWrapper();

                            // reset action bar
                            if (actionBar != null) {
                                actionBar.setDisplayHomeAsUpEnabled(false);
                                actionBar.setHomeButtonEnabled(false);
                            }
                            ac.getMDrawerToggle().setDrawerIndicatorEnabled(true);
                            ac.getMDrawerToggle().syncState();
                            ac.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                            fragmentView.setOnKeyListener(null);
                            return true;
                        }
                        return false;
                    }
                });

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Node n = nodeList.get(position);
                int nid = n.nid;
                ((MainActivity) getActivity()).setCurrentShareUrl(getString(R.string.siteurl) + "/node/" + nid, n.title);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPagerWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void fadeOutPagerWrapper(){
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
        viewPagerWrapper.startAnimation(fadeOutAnimation);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                viewPagerWrapper.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }

    private void fadeInPagerWrapper(){
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        viewPagerWrapper.startAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                viewPagerWrapper.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }


    private class ImagePagerAdapter extends PagerAdapter {

        @Override public int getCount() {
            return imageItems.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == ((SubsamplingScaleImageView) object);
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            Context context = getActivity();
            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setImage(ImageSource.bitmap(imageItems.get(position).getImage()));
            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((SubsamplingScaleImageView) object);
        }
    }


}
