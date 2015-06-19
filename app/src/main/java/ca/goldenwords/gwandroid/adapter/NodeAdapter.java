package ca.goldenwords.gwandroid.adapter;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.model.Node;


public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.NodeViewHolder>{

    List<Node> nodeList;
    Context context;
    private int lastPosition = -1;

    public NodeAdapter(List<Node> nodeList,Context c){
        this.nodeList = nodeList;
        this.context = c;
    }

    @Override public int getItemCount() {
        return nodeList.size();
    }

    @Override public void onBindViewHolder(NodeViewHolder contactViewHolder, int i) {
        Node n = nodeList.get(i);

        Date time=new java.util.Date((long)n.revision*1000);
        SimpleDateFormat ft = new SimpleDateFormat ("MMM d, y");

        if(n.cover_image==1){
            contactViewHolder.cover_image.setVisibility(View.VISIBLE);
            contactViewHolder.cover_image.setText(n.image_url);
        }else contactViewHolder.cover_image.setVisibility(View.INVISIBLE);

        contactViewHolder.card_headline.setText(n.title);
        contactViewHolder.card_details.setText(n.article_category+" - "+n.author+" - "+ft.format(time));

//        setAnimation(contactViewHolder.container, i);
    }

    @Override public NodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);
        return new NodeViewHolder(itemView);
    }

//    private void setAnimation(View viewToAnimate, int position){
//        if (position > lastPosition){
//            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
//            animation.setDuration(800);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }

    // ViewHolder Class
    public static class NodeViewHolder extends RecyclerView.ViewHolder  {
        protected TextView card_headline;
        protected TextView card_details;
        protected TextView cover_image;
        protected View container;

        public NodeViewHolder(View v) {
            super(v);
            card_headline =  (TextView) v.findViewById(R.id.card_headline);
            card_details = (TextView)  v.findViewById(R.id.card_details);
            cover_image = (TextView) v.findViewById(R.id.cover_image);
            container = v;
        }
    }// end NodeViewHolder

}
