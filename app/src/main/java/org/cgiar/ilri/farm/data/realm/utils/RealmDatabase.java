package org.cgiar.ilri.farm.data.realm.utils;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;

/**
 * This class has helper methods for the realm database
 *
 * Created by jrogena on 22/02/2016.
 */
public class RealmDatabase {
    private static final String TAG = "Sendy.RealmDatabase";
    /**
     * This method checks whether the provided realm object is open
     *
     * @param realm The realm object to be checked
     *
     * @return  True if the object is open
     */
    public static boolean isOpen(Realm realm) {
        if(realm != null && !realm.isClosed()) {
            return  true;
        }
        return false;
    }

    /**
     * This method creates a realm instance for the provided context that should be closed whenever
     * the context closes
     *
     * @param context   The context to initialize the realm object
     * @param realm     The realm object to be created
     *
     * @return  The realm object
     */
    public static Realm create(Context context, Realm realm) {
        if(!isOpen(realm)) {
            return Realm.getInstance(context);
        } else {
            Log.i(TAG, "Cannot create another realm object for instance. Existing realm object is open and fine");
        }
        return realm;
    }

    /**
     * This method closes the provided realm instance
     *
     * @param realm The realm instance to be closed
     *
     * @return  True if successful in closing the object
     */
    public static boolean close(Realm realm) {
        if(isOpen(realm)) {
            realm.close();
            return true;
        } else {
            Log.w(TAG, "The provided realm object for closing is already closed or null. Not closing it");
        }
        return false;
    }
}
