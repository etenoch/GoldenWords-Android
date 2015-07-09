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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.goldenwords.gwandroid.adapter.DrawerAdapter;
import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.events.ToastEvent;
import ca.goldenwords.gwandroid.fragments.CurrentIssueFragment;
import ca.goldenwords.gwandroid.utils.GWUtils;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements ActionMode.Callback {

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DrawerLayout drawer;

    private ActionBarDrawerToggle drawerToggle;
    private Fragment nextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GWUtils.setContext(this);
        DataCache.setContext(this);
        DataCache.setBirthDay((int) (System.currentTimeMillis() / 1000L));

        if (android.os.Build.VERSION.SDK_INT >=21)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // set up toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.startActionMode(this);
//        startSupportActionMode(this);

        // set up drawer
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new DrawerAdapter(getApplication().getResources().getStringArray(R.array.section_codes));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(nextFragment!=null) changeFragment(nextFragment);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // open first fragment
        Fragment nextFragment = new CurrentIssueFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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

    @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override public void onDestroyActionMode(ActionMode mode) {
    }

    public ActionBarDrawerToggle getMDrawerToggle(){
        return drawerToggle;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void changeFragment(Fragment nextFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, nextFragment).commit();
        this.nextFragment=null;
    }

    // Event bus events
    public void onEvent(Fragment nextFragment){  // set next fragment via EventBus
        this.nextFragment=nextFragment;
    }

    public void onEvent(ToastEvent toast){
        if(toast.isLength_long()) Toast.makeText(this,toast.getMessage(),Toast.LENGTH_LONG).show();
        else  Toast.makeText(this,toast.getMessage(),Toast.LENGTH_SHORT).show();
    }

}
