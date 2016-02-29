package org.cgiar.ilri.farm.data.realm.objects;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jrogena on 28/02/2016.
 */
public class Location extends RealmObject {
    @PrimaryKey
    private int id;
    private String level1;
    private String level2;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static RealmResults<Location> getAllLocations(Realm realm) {
        RealmResults<Location> results = realm.where(Location.class).findAll();
        return results;
    }

    public static RealmResults<Location> getLocations(Realm realm, int id) {
        RealmQuery<Location> query = realm.where(Location.class);
        query.equalTo("id", id);
        RealmResults<Location> locations = query.findAll();

        return locations;
    }
}
