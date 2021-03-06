package ca.goldenwords.gwandroid.data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.events.ToastEvent;
import ca.goldenwords.gwandroid.http.GenericFetcher;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.http.NodeFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.model.Section;
import ca.goldenwords.gwandroid.utils.GWUtils;
import ca.goldenwords.gwandroid.utils.RevisionDateComparator;
import ca.goldenwords.gwandroid.utils.Sections;
import ca.goldenwords.gwandroid.utils.VolumeIssueKey;
import de.greenrobot.event.EventBus;

public class DataCache {
    private static Context context;
    private static int unixTime;

    private static int currentVolume=-1;
    private static int currentIssue=-1;
    public final static HashSet<VolumeIssueKey> fullIssue = new HashSet<>();
    public final static ArrayMap<VolumeIssueKey,Set<Integer>> issueVolumeList = new ArrayMap<>();
    public final static ArrayMap<Sections,TreeSet<Node>> sectionCache = new ArrayMap<>();
    public final static ArrayMap<Integer,Node> nodeCache = new ArrayMap<>();

    public final static Set<AsyncTask> downloaderTasks = new HashSet<>();

    //=============================//
    //  Post to Bus (or Download)  //
    //=============================//

    public static AsyncTask postNodeToBus(int nid){
        Node n = nodeCache.get(nid);
        if(n!=null) {
            EventBus.getDefault().post(n);
            return null;
        }
        if(GWUtils.hasInternet()) return new NodeFetcher(context.getString(R.string.baseurl)+"/article/"+nid).execute();
        else EventBus.getDefault().post(new ToastEvent("No Internet Connection"));
        return null;
    }

    // -- Issues --
    public static AsyncTask postIssueToBus(){
        if(currentIssue >-1 && currentVolume>-1){
            return postIssueToBus(currentVolume, currentIssue);
        }
        return postIssueFetcher();
    }

    public static AsyncTask postIssueToBus(int volume_id,int issue_id){
        VolumeIssueKey key = new VolumeIssueKey(volume_id,issue_id);
        if(issueVolumeList.get(key)!=null && fullIssue.contains(key)){
            Set<Integer> nodeIdSet = issueVolumeList.get(key);
            Issue issue = new Issue();
            issue.nodes = new HashSet<>();

            for(int i:nodeIdSet){
                Node n = nodeCache.get(i);
                if(n==null) return postIssueFetcher(volume_id,issue_id);
                issue.nodes.add(n);
                issue.issue_id =n.issue_id;
                issue.volume_id =n.volume_id;
            }
            EventBus.getDefault().post(issue);
            return null;
        }
        return postIssueFetcher(volume_id, issue_id);
    }

    private static AsyncTask postIssueFetcher(int volume,int issue){
        if(GWUtils.hasInternet())
            return new ListFetcher(context.getString(R.string.baseurl)+"/issue/"+volume+"/"+issue,ListFetcher.Type.ISSUE).execute();
        else EventBus.getDefault().post(new ToastEvent("No Internet Connection"));
        return null;
    }
    private static AsyncTask postIssueFetcher(){
        if(GWUtils.hasInternet())
            return new ListFetcher(context.getString(R.string.baseurl)+"/issue",ListFetcher.Type.ISSUE).execute();
        else EventBus.getDefault().post(new ToastEvent("No Internet Connection"));
        return null;
    }

    // -- Sections --
    public static AsyncTask postSectionToBus(String localShortname) {
        Sections enumKey = GWUtils.parseCategoryShortname(localShortname);
        if (sectionCache.get(enumKey) != null) {
            // post first 10
            Section section = new Section();
            section.nodes = new TreeSet<>(new RevisionDateComparator());

            int count = 0;
            for (Node n: sectionCache.get(enumKey)){
                section.nodes.add(n);
                count++;
                if(count>=10) {
                    section.name = n.article_category;
                    break;
                }
            }
            section.articleCategory=enumKey;

            EventBus.getDefault().post(section);
            return null;
        }
        return postSectionFetcher(localShortname);
    }

    public static AsyncTask postSectionToBus(String localShortname, int offset){
        Sections enumKey = GWUtils.parseCategoryShortname(localShortname);
        if (sectionCache.get(enumKey) != null && sectionCache.get(enumKey).size()>=offset+10) {
            Section section = new Section();
            section.nodes = new TreeSet<>(new RevisionDateComparator());
            int count = 0;
            for (Node n: sectionCache.get(enumKey)){
                if(count > offset) section.nodes.add(n);
                count++;
                if(count-offset>10) {
                    section.name = n.article_category;
                    break;
                }
            }
            section.articleCategory=enumKey;
            EventBus.getDefault().post(section);
            return null;
        }
        return postSectionFetcher(localShortname,offset);
    }

    private static AsyncTask postSectionFetcher(String shortname){
        if(GWUtils.hasInternet())
            return new ListFetcher(context.getString(R.string.baseurl)+"/list/"+shortname,ListFetcher.Type.SECTION).execute();
        else EventBus.getDefault().post(new ToastEvent("No Internet Connection"));
        return null;
    }
    private static AsyncTask postSectionFetcher(String shortname,int offset){
        if(GWUtils.hasInternet())
            return new ListFetcher(context.getString(R.string.baseurl)+"/list/"+shortname+"/"+offset,ListFetcher.Type.SECTION).execute();
        else EventBus.getDefault().post(new ToastEvent("No Internet Connection"));
        return null;
    }


    // -- Locations --
    public static AsyncTask postLocations(){
        if(GWUtils.hasInternet())
            return new GenericFetcher(context.getString(R.string.baseurl)+"/locations").execute();
        else EventBus.getDefault().post(new ToastEvent("No Internet Connection"));
        return null;
    }


    //==================//
    //   Add to cache   //
    //==================//

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

    public static void addToCache(int volume, int issue, int nid){
        VolumeIssueKey key = new VolumeIssueKey(volume,issue);
        if(issueVolumeList.get(key)!=null){
            issueVolumeList.get(key).add(nid);
        }else{
            TreeSet<Integer> l = new TreeSet<>();
            l.add(nid);
            issueVolumeList.put(key,l);
        }
    }

    public static void trackFullIssue(VolumeIssueKey key){
        fullIssue.add(key);
    }

    public static void addToCache(Section section){
        if(sectionCache.get(section.articleCategory)!=null){
            sectionCache.get(section.articleCategory).addAll(section.nodes);
        }else{
            TreeSet<Node> l = new TreeSet<>(new RevisionDateComparator());
            l.addAll(section.nodes);
            sectionCache.put(section.articleCategory,l);
        }
    }



    //==================//
    //   Other Stuff    //
    //==================//


    public static void clearDownloaders(){
        for(AsyncTask t : downloaderTasks){
            if(t!=null) t.cancel(true);
        }
    }

    public static void setContext(Context context) {
        DataCache.context = context;
    }

    public static void setBirthDay(int unixTime) {
        DataCache.unixTime = unixTime;
    }

}



