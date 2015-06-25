package ca.goldenwords.gwandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.goldenwords.gwandroid.adapter.DrawerAdapter;
import ca.goldenwords.gwandroid.utils.CustomToast;
import ca.goldenwords.gwandroid.view.CurrentIssueFragment;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;
    Fragment nextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >=21)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DrawerAdapter(getApplication().getResources().getStringArray(R.array.section_codes));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(nextFragment!=null) changeFragment(nextFragment);
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        Fragment nextFragment = new CurrentIssueFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, nextFragment).commit();

    }

    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) { // action bar clicks
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ActionBarDrawerToggle getMDrawerToggle(){
        return mDrawerToggle;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }


    public void changeFragment(Fragment nextFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, nextFragment).commit();
        this.nextFragment=null;
    }

    public void onEvent(Fragment nextFragment){  // set next fragment via EventBus
        this.nextFragment=nextFragment;
    }

    public void onEvent(CustomToast toast){
        Toast.makeText(this,toast.getMessage(),Toast.LENGTH_LONG).show();
    }

}
