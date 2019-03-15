package in.notesmart.launcher.DataBase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import in.notesmart.launcher.Model.AppsDataPinned;

@Database(entities = AppsDataPinned.class, version = 1)
public abstract class AppsDataDB extends RoomDatabase {

    public static final String DATABSE_NAME = "appsDataDB";
    public static AppsDataDB instance;

    public static AppsDataDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppsDataDB.class, DATABSE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AppsDataDao appsDataDao();
}
