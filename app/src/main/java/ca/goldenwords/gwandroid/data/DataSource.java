package ca.goldenwords.gwandroid.data;


import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.http.NodeFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import ca.goldenwords.gwandroid.utils.VolumeIssueKey;
import de.greenrobot.event.EventBus;

public class DataSource {
    private static Context context;
    private static int unixTime;

    private static int currentVolume=-1;
    private static int currentIssue=-1;
    private final static HashMap<VolumeIssueKey,Set<Integer>> issueVolumeList = new HashMap<>();
    private final static HashMap<String,TreeSet<Node>> sectionList = new HashMap<>();
    private final static HashMap<Integer,Node> nodeCache = new HashMap<>();
    private final static HashMap<Integer,Bitmap> imageCache = new HashMap<>();

    public static void postNodeToBus(int nid){
        Node n = nodeCache.get(nid);
        if(n!=null) EventBus.getDefault().post(n);
        else{
            new NodeFetcher(context.getString(R.string.baseurl)+"/article/"+nid).execute();
        }
    }

    public static void postIssueToBus(){
        if(currentIssue >-1 && currentVolume>-1){
            postIssueToBus(currentVolume,currentIssue);
            return;
        }else{
            postIssueFetcher();
        }
    }

    public static void postIssueToBus(int volume_id,int issue_id){
        VolumeIssueKey key = new VolumeIssueKey(volume_id,issue_id);
        if(issueVolumeList.get(key)!=null){
            Set<Integer> nodeIdSet = issueVolumeList.get(key);
            Issue issue = new Issue();
            issue.nodes = new HashSet<>();

            for(int i:nodeIdSet){
                Node n = nodeCache.get(i);
                if(n==null){
                    postIssueFetcher(volume_id,issue_id);
                    return;
                }
                issue.nodes.add(n);
                issue.issue_id =n.issue_id;
                issue.volume_id =n.volume_id;
            }
            EventBus.getDefault().post(issue);
            return;
        }
        postIssueFetcher(volume_id,issue_id);
    }

    private static void postIssueFetcher(int volume,int issue){
        new ListFetcher(context.getString(R.string.baseurl)+"/issue/"+volume+"/"+issue,ListFetcher.Type.ISSUE).execute();
    }
    private static void postIssueFetcher(){
        new ListFetcher(context.getString(R.string.baseurl)+"/issue",ListFetcher.Type.ISSUE).execute();
    }

    public static void setContext(Context context) {
        DataSource.context = context;
    }

    public static void addToCache(Node node){
        nodeCache.put(node.nid, node);

        VolumeIssueKey key = new VolumeIssueKey(node.volume_id,node.issue_id);
        if(issueVolumeList.get(key)!=null){
            issueVolumeList.get(key).add(node.nid);
        }else{
            TreeSet<Integer> l = new TreeSet<>();
            l.add(node.nid);
            issueVolumeList.put(key,l);
        }

        if(node.issue_id>currentIssue && node.volume_id>currentVolume){
            currentIssue = node.issue_id;
            currentVolume = node.volume_id;
        }
    }

    public static void addToIssueCache(int volume, int issue, int nid){
        VolumeIssueKey key = new VolumeIssueKey(volume,issue);
        if(issueVolumeList.get(key)!=null){
            issueVolumeList.get(key).add(nid);
        }else{
            TreeSet<Integer> l = new TreeSet<>();
            l.add(nid);
            issueVolumeList.put(key,l);
        }
    }

    public static void addToCache(Section section){

    }



    public static int getBirthDay() {
        return unixTime;
    }

    public static void setBirthDay(int unixTime) {
        DataSource.unixTime = unixTime;
    }

}



