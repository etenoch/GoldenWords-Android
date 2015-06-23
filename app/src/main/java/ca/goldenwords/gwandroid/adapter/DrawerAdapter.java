package ca.goldenwords.gwandroid.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.view.ArticleListFragment;
import ca.goldenwords.gwandroid.view.CurrentIssueFragment;
import de.greenrobot.event.EventBus;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                itemView.setOnClickListener(this);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = 1;
            }
        }

        @Override public void onClick(View view) {
            final Activity host = (Activity) view.getContext();
            DrawerLayout dl = (DrawerLayout) host.findViewById(R.id.DrawerLayout);
            dl.closeDrawers();

            Object section = view.findViewById(R.id.rowText).getTag();
            Fragment fragment=null;
            if(section.toString().equals("current"))
                fragment = new CurrentIssueFragment();
            else if(section.toString().equals("news")||section.toString().equals("editorials")||section.toString().equals("random")){
                fragment = new ArticleListFragment();
                Bundle args = new Bundle();
                args.putString("section", section.toString());
                fragment.setArguments(args);
            }
            EventBus.getDefault().post(fragment);
        }

    }// class ViewHolder

    public DrawerAdapter(String titles[]){
        mNavTitles = titles;
    }

    @Override public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
            return new ViewHolder(v,viewType);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            return new ViewHolder(v,viewType);
        }
        return null;
    }

    @Override public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {
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

    @Override public int getItemCount() {
        return mNavTitles.length+1;
    }

    @Override public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}