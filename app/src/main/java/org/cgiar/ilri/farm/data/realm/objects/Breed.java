package org.cgiar.ilri.farm.data.realm.objects;

import io.realm.RealmObject;

/**
 * Created by jrogena on 28/02/2016.
 */
public class Breed extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
