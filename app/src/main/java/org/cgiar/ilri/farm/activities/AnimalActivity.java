package org.cgiar.ilri.farm.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.cgiar.ilri.farm.R;
import org.cgiar.ilri.farm.data.realm.utils.RealmDatabase;

import io.realm.Realm;

/**
 * Activity for showing animal information. The following extras can be passed to this activity:
 *  - KEY_ANIMAL_ID: Mandatory. ID for the animal to be displayed.
 */
public class AnimalActivity extends AppCompatActivity {
    private static final String TAG = "ILRI.AnimalActivity";
    public static final String KEY_ANIMAL_ID = "animal_id";
    private String animalId;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animalId = getIntent().getExtras().getString(KEY_ANIMAL_ID);
        setTitle(animalId);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    protected void onStop() {
        RealmDatabase.close(realm);
        super.onStop();
    }

    /**
     * This method initializes all the views in this activity
     */
    private void initViews() {

    }

    /**
     * This method refreshes data in this activity. Assumes that all the views have already been
     * initialized
     */
    private void refreshData() {
        realm = RealmDatabase.create(this, realm);
        if(RealmDatabase.isOpen(realm)) {

        }
    }
}
