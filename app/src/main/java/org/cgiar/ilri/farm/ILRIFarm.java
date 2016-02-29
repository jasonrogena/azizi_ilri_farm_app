package org.cgiar.ilri.farm;

import android.app.Application;
import android.util.Log;

import org.cgiar.ilri.farm.data.realm.objects.Animal;
import org.cgiar.ilri.farm.data.realm.utils.DatabaseMigration;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jrogena on 28/02/2016.
 */
public class ILRIFarm extends Application {
    private static final String TAG = "ILRI.ILRIFarm";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "**************************************************************");
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("ilri_farm.realm")
                .schemaVersion(DatabaseMigration.CURRENT_VERSION)
                .migration(new DatabaseMigration())
                .build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getInstance(config);
        realm.close();
        Log.d(TAG, "**************************************************************");
        writeTestData();
    }

    /**
     * TODO: remove this method
     */
    private void writeTestData() {
        String animals = "{\"status\":true,\"animals\":[{\"animal_id\":\"BJ052\",\"species\":\"Bovine\",\"sex\":\"Male\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\"]},{\"animal_id\":\"BJ053\",\"species\":\"Bovine\",\"sex\":\"Male\",\"status\":\"Alive\",\"location\":{\"id\":2,\"level_1\":\"Paddock\",\"level_2\":\"2\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ054\",\"species\":\"Bovine\",\"sex\":\"Male\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Friesian\"]},{\"animal_id\":\"BJ055\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":3,\"level_1\":\"Isolation Unit\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ056\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":3,\"level_1\":\"Isolation Unit\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ057\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":3,\"level_1\":\"Isolation Unit\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ058\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":3,\"level_1\":\"Isolation Unit\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ059\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":3,\"level_1\":\"Isolation Unit\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ044\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ045\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ046\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ047\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]},{\"animal_id\":\"BJ048\",\"species\":\"Bovine\",\"sex\":\"Female\",\"status\":\"Alive\",\"location\":{\"id\":1,\"level_1\":\"Paddock\",\"level_2\":\"1\",\"comment\":\"\"},\"breeds\":[\"Ankole\",\"Boran\"]}]}";
        try {
            JSONObject animalData = new JSONObject(animals);
            if(animalData.getBoolean("status")) {
                Animal.writeAnimals(this, animalData.getJSONArray("animals"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
