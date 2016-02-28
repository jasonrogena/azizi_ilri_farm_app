package org.cgiar.ilri.farm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.cgiar.ilri.farm.R;
import org.cgiar.ilri.farm.data.realm.objects.Animal;
import org.cgiar.ilri.farm.data.realm.objects.Breed;
import org.cgiar.ilri.farm.data.realm.objects.Location;
import org.cgiar.ilri.farm.data.realm.utils.RealmDatabase;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Activity for showing animal information. The following extras can be passed to this activity:
 *  - KEY_ANIMAL_ID: Mandatory. ID for the animal to be displayed.
 */
public class AnimalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ILRI.AnimalActivity";
    public static final String KEY_ANIMAL_ID = "animal_id";
    private String animalId;
    private Realm realm;
    private NetworkImageView animalImageNIV;
    private TextView animalIdTV, rfidTV, otherIdTV, statusTV;
    private TextView speciesTV, sexTV, breedsTV, dobTV;
    private TextView ownerTV, experimentTV, locLayer1TV, locLayer2TV, locCommentTV;
    private TextView damIdTV, damBreedsTV, damStatusTV;
    private TextView sireIdTV, sireBreedsTV, sireStatusTV;
    private LinearLayout damCanvasLL;
    private LinearLayout sireCanvasLL;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method initializes all the views in this activity
     */
    private void initViews() {
        animalImageNIV = (NetworkImageView)findViewById(R.id.animal_image_niv);

        animalIdTV = (TextView)findViewById(R.id.animal_id_tv);
        rfidTV = (TextView)findViewById(R.id.rfid_tv);
        otherIdTV = (TextView)findViewById(R.id.other_id_tv);
        statusTV = (TextView)findViewById(R.id.status_tv);

        speciesTV = (TextView)findViewById(R.id.species_tv);
        sexTV = (TextView)findViewById(R.id.sex_tv);
        breedsTV = (TextView)findViewById(R.id.breeds_tv);
        dobTV = (TextView)findViewById(R.id.dob_tv);

        ownerTV = (TextView)findViewById(R.id.owner_tv);
        experimentTV = (TextView)findViewById(R.id.experiment_tv);
        locLayer1TV = (TextView)findViewById(R.id.loc_layer_1_tv);
        locLayer2TV = (TextView)findViewById(R.id.loc_layer_2_tv);
        locCommentTV = (TextView)findViewById(R.id.loc_comment_tv);

        damCanvasLL = (LinearLayout)findViewById(R.id.dam_canvas_ll);
        damCanvasLL.setOnClickListener(this);
        damIdTV = (TextView)findViewById(R.id.dam_id_tv);
        damBreedsTV = (TextView)findViewById(R.id.dam_breeds_tv);
        damStatusTV = (TextView)findViewById(R.id.dam_status_tv);

        sireCanvasLL = (LinearLayout)findViewById(R.id.sire_canvas_ll);
        sireCanvasLL.setOnClickListener(this);
        sireIdTV = (TextView)findViewById(R.id.sire_id_tv);
        sireBreedsTV = (TextView)findViewById(R.id.sire_breeds_tv);
        sireStatusTV = (TextView)findViewById(R.id.sire_status_tv);
    }

    /**
     * This method refreshes data in this activity. Assumes that all the views have already been
     * initialized
     */
    private void refreshData() {
        realm = RealmDatabase.create(this, realm);
        if(RealmDatabase.isOpen(realm)) {
            RealmResults<Animal> animals = Animal.getAnimals(realm, animalId);
            if(animals != null && animals.size() == 1) {
                Animal animal = animals.get(0);
                animalIdTV.setText("Animal ID: "+animal.getAnimalId());
                rfidTV.setText("RFID: ");
                statusTV.setText("Status: " + animal.getStatus());

                animalImageNIV.setDefaultImageResId(R.drawable.bull);

                speciesTV.setText("Species: " + animal.getSpecies().getName());
                sexTV.setText("Sex: " + animal.getSex().getName());
                RealmList<Breed> breeds = animal.getBreeds();
                if(breeds != null && breeds.size() > 0) {
                    breedsTV.setVisibility(View.VISIBLE);
                    String breedText = "";
                    for(int i = 0; i < breeds.size(); i++) {
                        if(breedText.length() > 0) {
                            breedText = breedText + " | ";
                        }

                        breedText = breedText + breeds.get(i).getName();
                    }
                    breedsTV.setText("Breed: "+breedText);
                } else {
                    breedsTV.setVisibility(View.VISIBLE);
                    breedsTV.setText("Breed: Unknown");
                }

                dobTV.setText("DoB: Unknown");
                ownerTV.setText("Owner: Unknown");
                experimentTV.setText("Experiment: None");

                Location location = animal.getLocation();
                locLayer1TV.setText(location.getLevel1());
                locLayer2TV.setText(location.getLevel2());
                if(location.getComment() != null && location.getComment().length() > 0) {
                    locCommentTV.setVisibility(View.VISIBLE);
                    locCommentTV.setText(location.getComment());
                } else {
                    locCommentTV.setVisibility(View.GONE);
                }

                Animal dam = Animal.getDam(realm, animal);
                if(dam != null) {
                    damCanvasLL.setVisibility(View.VISIBLE);
                    damIdTV.setText(dam.getAnimalId());
                    damStatusTV.setText(dam.getStatus());

                    RealmList<Breed> damBreed = dam.getBreeds();
                    if(damBreed != null && damBreed.size() > 0) {
                        String breedText = "";
                        for(int i = 0; i < damBreed.size(); i++) {
                            if(breedText.length() > 0) {
                                breedText = breedText + ", ";
                            }

                            breedText = breedText + damBreed.get(i).getName();
                        }
                        damBreedsTV.setText(breedText);
                    } else {
                        damBreedsTV.setText("Unknown breed");
                    }
                } else {
                    damCanvasLL.setVisibility(View.GONE);
                }

                Animal sire = Animal.getSire(realm, animal);
                if(sire != null) {
                    sireCanvasLL.setVisibility(View.VISIBLE);
                    sireIdTV.setText(sire.getAnimalId());
                    sireStatusTV.setText(sire.getStatus());

                    RealmList<Breed> sireBreed = sire.getBreeds();
                    if(sireBreed != null && sireBreed.size() > 0) {
                        String breedText = "";
                        for(int i = 0; i < sireBreed.size(); i++) {
                            if(breedText.length() > 0) {
                                breedText = breedText + ", ";
                            }

                            breedText = breedText + sireBreed.get(i).getName();
                        }
                        sireBreedsTV.setText(breedText);
                    } else {
                        sireBreedsTV.setText("Unknown breed");
                    }
                } else {
                    sireCanvasLL.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(sireCanvasLL)) {
            if(RealmDatabase.isOpen(realm)) {
                RealmResults<Animal> animals = Animal.getAnimals(realm, animalId);
                if(animals != null && animals.size() == 1) {
                    Animal animal = animals.get(0);
                    Animal sire = Animal.getSire(realm, animal);
                    Intent intent = new Intent(this, AnimalActivity.class);
                    intent.putExtra(KEY_ANIMAL_ID, sire.getAnimalId());
                    startActivity(intent);
                }
            }
        } else if(v.equals(damCanvasLL)) {
            if(RealmDatabase.isOpen(realm)) {
                RealmResults<Animal> animals = Animal.getAnimals(realm, animalId);
                if(animals != null && animals.size() == 1) {
                    Animal animal = animals.get(0);
                    Animal dam = Animal.getDam(realm, animal);
                    Intent intent = new Intent(this, AnimalActivity.class);
                    intent.putExtra(KEY_ANIMAL_ID, dam.getAnimalId());
                    startActivity(intent);
                }
            }
        }
    }
}
