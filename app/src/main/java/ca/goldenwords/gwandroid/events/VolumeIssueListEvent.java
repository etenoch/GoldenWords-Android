package ca.goldenwords.gwandroid.events;


import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class VolumeIssueListEvent {

    private TreeMap<Integer,ArrayList<Integer>> volumeIssueListMap;

    public VolumeIssueListEvent() {
        this.volumeIssueListMap = new TreeMap<>(Collections.reverseOrder());
    }

    public VolumeIssueListEvent addVolume(int volume,ArrayList<Integer> issues){
        if(volumeIssueListMap.get(volume)==null){
            volumeIssueListMap.put(volume,issues);
        }else{
            for(int i : issues){
                volumeIssueListMap.get(volume).add(i);
            }
        }
        return this;
    }
    public VolumeIssueListEvent addIssue(int volume,int issue){
        if(volumeIssueListMap.get(volume)!=null){
            ArrayList<Integer> newList = new ArrayList<>();
            newList.add(issue);
            volumeIssueListMap.put(volume,newList);
        }
        return this;
    }


    public TreeMap<Integer, ArrayList<Integer>> getVolumeIssueListMap() {
        return volumeIssueListMap;
    }

    public void setVolumeIssueListMap(TreeMap<Integer, ArrayList<Integer>> volumeIssueListMap) {
        this.volumeIssueListMap = volumeIssueListMap;
    }
}

