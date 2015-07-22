package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.goldenwords.gwandroid.R;


public class LocationsFragment extends Fragment {

    private View fragmentView;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_map_locations, container, false);
        return fragmentView;
    }


}
