package ca.goldenwords.gwandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.goldenwords.gwandroid.R;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.StringWrapperEvent;
import de.greenrobot.event.EventBus;


public class LocationsFragment extends Fragment implements OnMapReadyCallback{

    private View fragmentView;

    private MapFragment mMapFragment;
    private GoogleMap map;

    private JSONArray locations;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_map_locations, container, false);
        EventBus.getDefault().register(this);
        DataCache.postLocations();

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        return fragmentView;
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onMapReady(GoogleMap map){
        this.map = map;
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.22838027067406, -76.49507761001587), 16));

        try{
            JSONObject obj;
            JSONArray coor;
            for(int i = 0; i<locations.length();i++){
                obj = locations.getJSONObject(i);
                coor = obj.getJSONArray("coordinates");
                map.addMarker(new MarkerOptions().position(new LatLng(coor.getDouble(1), coor.getDouble(0))).title(obj.getString("name")));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public void onEvent(StringWrapperEvent string){
        try{
            locations = new JSONArray(string.getData());

            mMapFragment.getMapAsync(this);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

}
