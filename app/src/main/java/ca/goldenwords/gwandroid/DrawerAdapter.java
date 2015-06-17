package ca.goldenwords.gwandroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.goldenwords.gwandroid.view.ArticleListFragment;
import ca.goldenwords.gwandroid.view.CurrentIssueFragment;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            if(ViewType == TYPE_ITEM) {
                itemView.setOnClickListener(this);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = 1;
            }
        }

        @Override
        public void onClick(View view) {
            final Activity host = (Activity) view.getContext();
            DrawerLayout dl = (DrawerLayout) host.findViewById(R.id.DrawerLayout);
            dl.closeDrawers();

            Object section = view.findViewById(R.id.rowText).getTag();
            Fragment fragment=null;
            if(section.toString().equals("current"))
                fragment = new CurrentIssueFragment();
            else if(section.toString().equals("news"))
                fragment = new ArticleListFragment();


            if(fragment != null){
                final Fragment nextFragment = fragment;
                final FragmentActivity activity = (FragmentActivity)view.getContext();
                final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

                Runnable r = new Runnable() {
                    public void run() {
                        ft.replace(R.id.fragment_container, nextFragment).commit();
                    }
                };
                new Handler().postDelayed(r, 80); // run on new thread with a slight delay to avoid animation stutters

            }

        }

    }

    public DrawerAdapter(String titles[]){
        mNavTitles = titles;

    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view
            return vhItem; // Returning the created object
            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view
            return vhHeader; //returning the object created
        }
        return null;
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            try{
                int id = R.string.class.getField(mNavTitles[position - 1]).getInt(null);
                holder.textView.setText(id); // Setting the Text with the array of our Titles
                holder.textView.setTag(mNavTitles[position - 1]);
            }catch(NoSuchFieldException ex){
                ex.printStackTrace();
            }catch(IllegalAccessException ex){
                ex.printStackTrace();
            }
        }

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}