package com.example.asif.useractivityrecognition;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Asif on 23/12/2017.
 */

public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("userActivity")
                .build();
        Realm.setDefaultConfiguration(config);

    }

}
