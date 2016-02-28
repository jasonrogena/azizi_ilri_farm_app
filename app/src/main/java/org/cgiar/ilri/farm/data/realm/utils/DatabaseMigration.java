package org.cgiar.ilri.farm.data.realm.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by Jason Rogena on 25/01/2016.
 */
public class DatabaseMigration implements RealmMigration {
    public static final int CURRENT_VERSION = 1;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
    }
}
