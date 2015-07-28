package ca.goldenwords.gwandroid.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.events.BrowserIntentEvent;
import ca.goldenwords.gwandroid.fragments.ArticleViewFragment;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Node;
import de.greenrobot.event.EventBus;


public class VideoLinkAdapter extends RecyclerView.Adapter<VideoLinkAdapter.NodeViewHolder>{

    List<Node> nodeList;
    Context context;
    HashMap<Integer,NodeViewHolder> persistentViews = new HashMap<>();

    public VideoLinkAdapter(List<Node> nodeList,Context c){
        this.nodeList = nodeList;
        this.context = c;
    }

    @Override public int getItemCount() {
        return nodeList.size();
    }

    @Override public void onBindViewHolder(NodeViewHolder viewHolder, int i) {
        Node n = nodeList.get(i);
        viewHolder.setVideoUrl(n.video_url);

        Date time=new java.util.Date((long)n.revision_timestamp*1000);
        SimpleDateFormat ft = new SimpleDateFormat("MMM d, y");

        viewHolder.card_headline.setText(n.title);
        String authorstring = n.author!=null && !n.author.isEmpty() ? n.author + " - " :"";
        viewHolder.card_details.setText(authorstring + ft.format(time));

        persistentViews.put(i, viewHolder);

    }

    @Override public NodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_link_card_view, viewGroup, false);
        NodeViewHolder viewHolder =new NodeViewHolder(itemView);
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }


    // ViewHolder Class
    public class NodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView card_headline;
        protected TextView card_details;
        protected View container;

        protected String videoUrl;

        public NodeViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            card_headline =  (TextView) v.findViewById(R.id.card_headline);
            card_details = (TextView)  v.findViewById(R.id.card_details);
            container = v;
        }

        public void setVideoUrl(String videoUrl){
            this.videoUrl = videoUrl;
        }

        @Override public void onClick(View view) {
            EventBus.getDefault().post(new BrowserIntentEvent(videoUrl));
        }

    }// end NodeViewHolder

}

