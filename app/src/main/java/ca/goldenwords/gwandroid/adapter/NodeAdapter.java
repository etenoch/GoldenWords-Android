package ca.goldenwords.gwandroid.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.fragments.ArticleViewFragment;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Node;
import de.greenrobot.event.EventBus;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.NodeViewHolder>{

    List<Node> nodeList;
    Context context;
    private int lastPosition = -1;
    private ListFetcher.Type type;
    HashMap<Integer,NodeViewHolder> persistentViews = new HashMap<>();

    Fragment theFragment;

    public NodeAdapter(List<Node> nodeList,Context c,ListFetcher.Type type,Fragment theFragment){
        this.nodeList = nodeList;
        this.context = c;
        this.type=type;
        this.theFragment=theFragment;
    }

    @Override public int getItemCount() {
        return nodeList.size();
    }

    @Override public void onBindViewHolder(NodeViewHolder viewHolder, int i) {
        Node n = nodeList.get(i);
        viewHolder.setNodeData(n);

        Date time=new java.util.Date((long)n.revision_timestamp*1000);
        SimpleDateFormat ft = new SimpleDateFormat("MMM d, y");

        if(n.cover_image==1){
            viewHolder.cover_image.setVisibility(View.VISIBLE);
            viewHolder.cover_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(context).load(n.image_url).placeholder(R.drawable.ic_placeholder).into(viewHolder.cover_image);
        }else viewHolder.cover_image.setVisibility(View.GONE);

        viewHolder.card_headline.setText(n.title);
        if(type == ListFetcher.Type.ISSUE) {
            String authorstring = n.author!=null && !n.author.isEmpty() ? " - " + n.author + " - " : " - ";
            viewHolder.card_details.setText(n.article_category + authorstring + ft.format(time));
        }if(type == ListFetcher.Type.SECTION) {
            String authorstring = n.author!=null && !n.author.isEmpty() ? n.author + " - " :"";
            viewHolder.card_details.setText(authorstring + ft.format(time));
        }
        persistentViews.put(i, viewHolder);

    }

    @Override public NodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        NodeViewHolder viewHolder =new NodeViewHolder(itemView);
        //if(type== ListFetcher.Type.ISSUE)
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }


    // ViewHolder Class
    public class NodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView card_headline;
        protected TextView card_details;
        protected ImageView cover_image;
        protected View container;
        protected Node nodeData;

        public NodeViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            card_headline =  (TextView) v.findViewById(R.id.card_headline);
            card_details = (TextView)  v.findViewById(R.id.card_details);
            cover_image = (ImageView) v.findViewById(R.id.cover_image);
            container = v;
        }

        public void setNodeData(Node node){
            nodeData=node;
        }

        @Override public void onClick(View view) {
            Fragment fragment = new ArticleViewFragment();
            FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            ft.hide(theFragment);
            ft.add(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
            ((AppCompatActivity)context).getSupportFragmentManager().executePendingTransactions();

            EventBus.getDefault().post(nodeData);
        }

    }// end NodeViewHolder

}
