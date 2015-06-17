package ca.goldenwords.gwandroid.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.controller.IssueFetcher;
import ca.goldenwords.gwandroid.model.Issue;
import de.greenrobot.event.EventBus;

public class CurrentIssueFragment extends Fragment {

    public CurrentIssueFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_issue, container, false);
        new IssueFetcher("http://goldenwords.ca/api/get/issue/49/25").execute();
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
        String title = currentIssue.nodes.iterator().next().title;

        setText(title);
    }

    public void setText(String text){
        TextView tv = (TextView) getView().findViewById(R.id.testText);
        tv.setText(text);

    }


}
