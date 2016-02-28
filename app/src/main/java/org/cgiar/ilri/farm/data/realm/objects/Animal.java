package org.cgiar.ilri.farm.data.realm.objects;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.cgiar.ilri.farm.data.realm.utils.RealmDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jrogena on 28/02/2016.
 */
public class Animal extends RealmObject {
    private static final String TAG = "ILRI.Animal";
    @PrimaryKey
    private String animalId;
    private Species species;
    private RealmList<Breed> breeds;//nullable
    private Sex sex;
    private String status;
    private Location location;//nullable

    public Animal(String animalId) {
        this.animalId = animalId;
    }

    public Animal() {

    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public RealmList<Breed> getBreeds() {
        return breeds;
    }

    public void setBreeds(RealmList<Breed> breeds) {
        this.breeds = breeds;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * This method creates an Animal Realm object using the provided data. The returned object is not
     * yet in the database but should be intended for a write
     *
     * @param realm The realm object to be used to generate the animal object
     * @param data  JSONObject containing the data
     *
     * @return  The generated Realm animal object or null if an error occurred
     * @throws JSONException
     */
    private static Animal getAnimalForWrite(Realm realm, JSONObject data) throws JSONException{
        if(data.has("animal_id")) {
            Animal animal = new Animal(data.getString("animal_id"));
            Species species = realm.createObject(Species.class);
            species.setName(data.getString("species"));
            animal.setSpecies(species);

            Sex sex = realm.createObject(Sex.class);
            sex.setName(data.getString("sex"));
            animal.setSex(sex);

            animal.setStatus(data.getString("status"));

            if(data.has("breeds")) {
                JSONArray breedData = data.getJSONArray("breeds");
                RealmList<Breed> breeds = new RealmList<>();
                for(int i = 0; i < breedData.length(); i++) {
                    Breed currBreed = realm.createObject(Breed.class);
                    currBreed.setName(breedData.getString(i));
                    breeds.add(currBreed);
                }
                animal.setBreeds(breeds);
            }

            if(data.has("location")) {
                JSONObject locData = data.getJSONObject("location");
                Location location = realm.createObject(Location.class);
                location.setLevel1(locData.getString("level_1"));
                location.setLevel2(locData.getString("level_2"));
                location.setComment(locData.getString("comment"));
                animal.setLocation(location);
            }

            return animal;
        }
        return null;
    }

    /**
     * This method writes the provided JSONArray of animal data into the Realm database
     *
     * @param context   The context from where this method is called
     * @param data      The JSONArray containing the data
     * @return  A list of the IDs of the animals that were added
     * @throws JSONException
     */
    public static ArrayList<String> writeAnimals(Context context, JSONArray data) throws JSONException{
        ArrayList<String> ids = new ArrayList<>();
        Realm realm = RealmDatabase.create(context, null);
        realm.beginTransaction();
        Log.d(TAG, "------------------------ realm transaction started ------------------------");
        for(int i = 0; i < data.length(); i++) {
            Animal currAnimal = getAnimalForWrite(realm, data.getJSONObject(i));
            if(currAnimal != null) {
                RealmResults<Animal> existingAnimals = getAnimals(realm, currAnimal.animalId);
                if(existingAnimals != null) {
                    existingAnimals.clear();
                }

                realm.copyToRealmOrUpdate(currAnimal);
                ids.add(currAnimal.animalId);
            }
        }
        Log.d(TAG, "------------------------ realm transaction ended ------------------------");
        realm.commitTransaction();
        RealmDatabase.close(realm);
        return ids;
    }

    /**
     * This method returns a list of animals that match the provided animalId. Note that when the
     * provided realm object is closed, the list obtained from this method will be invalidated (not
     *  accessible)
     *
     * @param realm     The realm object to be used to read the data
     * @param animalId  The animal id to be used to search
     * @return  RealmResults containing the animals or null if an error occurred
     */
    public static RealmResults<Animal> getAnimals(Realm realm, String animalId) {
        RealmQuery<Animal> query = realm.where(Animal.class);
        query.equalTo("animalId", animalId);
        RealmResults<Animal> animals = query.findAll();

        return animals;
    }

    public static RealmResults<Animal> getAllAnimals(Realm realm) {
        RealmResults<Animal> results = realm.where(Animal.class).findAll();
        return results;
    }

    public static Animal getSire(Realm realm, Animal animal) {
        //TODO: get sire from animal
        String sireId = "BJ054";
        RealmResults<Animal> sires = Animal.getAnimals(realm, sireId);
        if(sires != null && sires.size() == 1) {
            Animal sire = sires.get(0);
            return sire;
        }
        return null;
    }

    public static Animal getDam(Realm realm, Animal animal) {
        //TODO: get dam from animal
        String sireId = "BJ053";
        RealmResults<Animal> dams = Animal.getAnimals(realm, sireId);
        if(dams != null && dams.size() == 1) {
            Animal dam = dams.get(0);
            return dam;
        }
        return null;
    }
}
