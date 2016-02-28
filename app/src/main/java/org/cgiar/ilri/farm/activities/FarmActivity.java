package org.cgiar.ilri.farm.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.cgiar.ilri.farm.R;
import org.cgiar.ilri.farm.data.realm.objects.Animal;
import org.cgiar.ilri.farm.data.realm.utils.RealmDatabase;
import org.cgiar.ilri.farm.ui.adapters.AnimalListAdapter;

import io.realm.Realm;
import io.realm.RealmResults;

public class FarmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private static final String TAG = "ILRIFarm.Farm";
    private RecyclerView animalListRV;
    private AnimalListAdapter animalListAdapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initViews();
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
    }

    /**
     * This method refreshes the data in all the views. It assumes that the views have already been
     * initialized
     */
    private void refreshData() {
        realm = RealmDatabase.create(this, realm);
        if(RealmDatabase.isOpen(realm)) {
            RealmResults<Animal> allAnimals = Animal.getAllAnimals(realm);
            animalListAdapter.addAll(allAnimals);
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
}
