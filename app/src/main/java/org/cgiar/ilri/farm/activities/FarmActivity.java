package org.cgiar.ilri.farm.activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.cgiar.ilri.farm.R;
import org.cgiar.ilri.farm.data.realm.objects.Animal;
import org.cgiar.ilri.farm.data.realm.objects.Location;
import org.cgiar.ilri.farm.data.realm.utils.RealmDatabase;
import org.cgiar.ilri.farm.ui.adapters.AnimalListAdapter;
import org.cgiar.ilri.farm.ui.adapters.LocationsToolbarAdapter;
import org.cgiar.ilri.farm.utils.Display;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FarmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String TAG = "ILRIFarm.Farm";
    private RecyclerView animalListRV;
    private AnimalListAdapter animalListAdapter;
    private Realm realm;
    private ActionBar actionBar;
    private Spinner toolbarSpinnerS;
    private LocationsToolbarAdapter toolbarSpinnerAdapter;
    private FloatingActionButton recordFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        initToolBar();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initViews();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarSpinnerS = (Spinner) findViewById(R.id.toolbar_spinner_s);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    protected void onDestroy() {
        RealmDatabase.close(realm);
        super.onDestroy();
    }

    /**
     * This method initializes the views in this activity
     */
    private void initViews() {
        animalListAdapter = new AnimalListAdapter(this);
        animalListRV = (RecyclerView)findViewById(R.id.animal_list_rv);
        animalListRV.setAdapter(animalListAdapter);
        animalListRV.setLayoutManager(new LinearLayoutManager(this));
        recordFAB = (FloatingActionButton)findViewById(R.id.record_fab);
        recordFAB.setOnClickListener(this);
    }

    /**
     * This method refreshes the data in all the views. It assumes that the views have already been
     * initialized
     */
    private void refreshData() {
        realm = RealmDatabase.create(this, realm);
        if(RealmDatabase.isOpen(realm)) {
            refreshToolbarData();
            RealmResults<Animal> allAnimals = Animal.getAllAnimals(realm);
            animalListAdapter.addAll(allAnimals);
        }
    }

    private void refreshToolbarData() {
        if(RealmDatabase.isOpen(realm)) {
            RealmResults<Location> locations = Location.getAllLocations(realm);
            ArrayList<LocationsToolbarAdapter.Location> items = new ArrayList<>();
            //the first item in the list should always be the app name representing all the animals
            items.add(new LocationsToolbarAdapter.Location(getResources().getString(R.string.app_name), null));
            if(locations != null) {
                for (int i = 0; i < locations.size(); i++) {
                    LocationsToolbarAdapter.Location currLoc = new LocationsToolbarAdapter.Location(locations.get(i).getId(), locations.get(i).getLevel1(), locations.get(i).getLevel2());
                    items.add(currLoc);
                }
            }
            toolbarSpinnerAdapter = new LocationsToolbarAdapter(this, R.layout.view_location_spinner_item, R.layout.view_location_spinner_dropdown, items);
            toolbarSpinnerS.setAdapter(toolbarSpinnerAdapter);
            toolbarSpinnerS.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.farm, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        /*// Assumes current activity is the searchable activity
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default*/
        if(searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_farm) {
            //TODO: refresh data
        } else if (id == R.id.nav_account) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called whenever query text is submitted in the search action's layout
     *
     * @param query The submitted query
     * @return  True if successful when searching
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called whenever the text changes in the search action's layout
     *
     * @param newText   The new text
     * @return  True if successful
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, newText);
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.equals(toolbarSpinnerS)) {
            if(toolbarSpinnerAdapter != null) {
                LocationsToolbarAdapter.Location location = toolbarSpinnerAdapter.getItem(position);
                if(location.getId() == LocationsToolbarAdapter.Location.DEFAULT_ID) {//user wants all the animals
                    animalListAdapter.clear();
                    RealmResults<Animal> allAnimals = Animal.getAllAnimals(realm);
                    animalListAdapter.addAll(allAnimals);
                } else {
                    animalListAdapter.clear();
                    RealmResults<Animal> animals = Animal.getAnimalsInLocation(realm, location.getId());
                    animalListAdapter.addAll(animals);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.equals(recordFAB)) {
            showRecordDialog();
        }
    }

    private void showRecordDialog() {
        AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.getWindow().setLayout(Display.getWidth(this), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.dialog_farm_events);
        dialog.setTitle(getResources().getString(R.string.title_record));
        dialog.show();
    }
}
