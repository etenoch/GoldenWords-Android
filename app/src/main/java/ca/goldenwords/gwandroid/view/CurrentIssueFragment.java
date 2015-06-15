package ca.goldenwords.gwandroid.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.controller.CurrentIssueFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import de.greenrobot.event.EventBus;

public class CurrentIssueFragment extends Fragment {

    public CurrentIssueFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_issue, container, false);
        new CurrentIssueFetcher().execute();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(Issue currentIssue){
        setText(currentIssue.getData());
    }

    public void setText(String text){
        TextView tv = (TextView) getView().findViewById(R.id.testText);
        tv.setText(text);

    }


}
