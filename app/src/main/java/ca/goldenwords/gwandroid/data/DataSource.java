package ca.goldenwords.gwandroid.data;


import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.TreeMap;
import java.util.TreeSet;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.http.NodeFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import ca.goldenwords.gwandroid.utils.IssueVolumeKey;
import de.greenrobot.event.EventBus;

public class DataSource {
    private static Context context;

    private final static HashMap<IssueVolumeKey,ArrayList<Integer>> issueVolumeList = new HashMap<>();
    private final static HashMap<String,TreeSet<Node>> sectionList = new HashMap<>();
    private final static HashMap<Integer,Node> dataCache = new HashMap<>();
    private final static HashMap<Integer,Bitmap> imageCache = new HashMap<>();

    public static void postNodeToBus(int nid){
        Node n = dataCache.get(nid);
        if(n!=null) EventBus.getDefault().post(n);
        else{
            new NodeFetcher(context.getString(R.string.baseurl)+"/article/"+nid).execute();
        }
    }

    public static void setContext(Context context) {
        DataSource.context = context;
    }

    public static void addToCache(Node node){
        dataCache.put(node.nid,node);
    }

    public static void addToCache(Issue issue){

    }

    public static void addToCache(Section section){

    }

}



