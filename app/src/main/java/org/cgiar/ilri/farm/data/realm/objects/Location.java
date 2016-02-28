package org.cgiar.ilri.farm.data.realm.objects;

import io.realm.RealmObject;

/**
 * Created by jrogena on 28/02/2016.
 */
public class Location extends RealmObject {
    private String level1;
    private String level2;
    private String comment;

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
}
