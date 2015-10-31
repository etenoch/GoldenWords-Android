package ca.goldenwords.gwandroid.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ca.goldenwords.gwandroid.MainActivity;
import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.adapter.NodeAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.VolumeIssueListEvent;
import ca.goldenwords.gwandroid.http.GenericFetcher;
import ca.goldenwords.gwandroid.http.ListFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import ca.goldenwords.gwandroid.model.Node;
import ca.goldenwords.gwandroid.utils.VolumeIssueKey;
import de.greenrobot.event.EventBus;

public class CurrentIssueFragment extends Fragment {

    private View fragmentView;
    private boolean dataLoaded = false;

    private TextView header;
    private AlertDialog.Builder dialogBuilder;

    public CurrentIssueFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return getPersistentView(inflater,container,savedInstanceState);
    }

    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null || header == null) {
            ((MainActivity)getActivity()).setCurrentShareUrl(getString(R.string.siteurl),"Golden Words");

            fragmentView = inflater.inflate(R.layout.fragment_current_issue, container, false);

            header = (TextView) fragmentView.findViewById(R.id.volume_issue_header);

            dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setIcon(R.mipmap.ic_launcher);
            dialogBuilder.setTitle("Volumes and Issues");

            dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            new GenericFetcher(getActivity().getString(R.string.vi_list)){

                @Override protected void onPostExecute(String result) {
                    VolumeIssueListEvent vile = new VolumeIssueListEvent();

                    try{
                        JSONArray vList = new JSONArray(result);
                        JSONObject vol;

                        for(int i=0;i<vList.length();i++){
                            vol = vList.getJSONObject(i);

                            int vid = vol.getInt("Volume");
                            ArrayList<Integer> issues = new ArrayList<Integer>();

                            for (int j=0;j<vol.getJSONArray("Issues").length();j++) issues.add(vol.getJSONArray("Issues").getInt(j));

                            Collections.reverse(issues);
                            vile.addVolume(vid,issues);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    EventBus.getDefault().post(vile);
                }
            }.execute();

            if(getArguments()==null) DataCache.downloaderTasks.add(DataCache.postIssueToBus());
            else DataCache.downloaderTasks.add(DataCache.postIssueToBus(getArguments().getInt("volume",0),getArguments().getInt("issue",0)));
        }

        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        DataCache.clearDownloaders();
        if(!dataLoaded) fragmentView = null;
        dataLoaded = false;
        super.onStop();
    }

    // event bus handler
    // data has loaded
    public void onEvent(Issue currentIssue){
        if(!dataLoaded) {
            ProgressBar loading_spinner = (ProgressBar)fragmentView.findViewById(R.id.loading_spinner);

            TextView volume_issue_header = (TextView)fragmentView.findViewById(R.id.volume_issue_header);
            volume_issue_header.setText("Volume " + currentIssue.volume_id + " - Issue " + currentIssue.issue_id);

            RecyclerView recList = (RecyclerView) fragmentView.findViewById(R.id.cards_list);
            recList.setLayoutManager(new LinearLayoutManager(getActivity()));

            List<Node> nodes = new ArrayList<>();

            NodeAdapter adp = new NodeAdapter(nodes,getActivity(),ListFetcher.Type.ISSUE,this);

            // sort HashMap into List for adapting
            for (Node s : currentIssue.nodes) {
                if(s.cover_image==1) nodes.add(0, s); // put cover image first
                else nodes.add(s);
            }

            recList.setVisibility(View.VISIBLE);
            recList.setAdapter(adp);
            volume_issue_header.setVisibility(View.VISIBLE);
            loading_spinner.setVisibility(View.INVISIBLE);
            dataLoaded=true;
        }

    }

    public void onEvent(VolumeIssueListEvent e){

        final HashMap<Integer,VolumeIssueKey> orderedKeys = new HashMap<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.select_dialog_item);

        int i = 0;
        for (int vid : e.getVolumeIssueListMap().keySet()){
            for(int issue: e.getVolumeIssueListMap().get(vid)){
                arrayAdapter.add(" Volume "+vid+" - Issue "+issue);
                orderedKeys.put(i,new VolumeIssueKey(vid,issue));
                i++;
            }
        }

        dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                VolumeIssueKey vi = orderedKeys.get(which);

                Bundle bundle = new Bundle();
                bundle.putInt("volume", vi.getVolume());
                bundle.putInt("issue",vi.getIssue());

                CurrentIssueFragment newIssue = new CurrentIssueFragment();
                newIssue.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragment_container, newIssue).commit();

                dialog.dismiss();
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBuilder.show();
            }
        });
    }

}
