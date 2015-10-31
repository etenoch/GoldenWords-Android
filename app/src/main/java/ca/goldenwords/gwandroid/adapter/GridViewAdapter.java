package ca.goldenwords.gwandroid.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.model.ImageItem;

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else holder = (ViewHolder) row.getTag();

        ImageItem item = data.get(position);

        if(item.nid==-1){ // load more
            holder.image.setImageResource(R.drawable.ic_loadmore_sq);
        }else{
            String url = DataCache.nodeCache.get(item.nid).image_url;
            Picasso.with(context).load(url).resize(260,260).centerCrop().placeholder(R.drawable.ic_placeholder_sq).into(holder.image);
        }

        return row;
    }

    static class ViewHolder {
        public ImageView image;
    }
}
