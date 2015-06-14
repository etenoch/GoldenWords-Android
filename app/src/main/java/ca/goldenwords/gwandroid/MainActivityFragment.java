package ca.goldenwords.gwandroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ca.goldenwords.gwandroid.controller.TestAsyncTask;
import de.greenrobot.event.EventBus;


public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        new TestAsyncTask().execute();
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

    public void onEvent(MessageEvent event){
        Toast.makeText(getActivity(), "woo", Toast.LENGTH_LONG).show();
        setText(event.message);
    }

    public void setText(String text){
        TextView tv = (TextView) getView().findViewById(R.id.testText);
        tv.setText(text);

    }


}
