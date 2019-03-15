package in.notesmart.launcher.DataBase;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import in.notesmart.launcher.Model.AppsDataPinned;

@Dao
public interface AppsDataDao {

    /**
     * @param appsDataPinned
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertAppsData(AppsDataPinned appsDataPinned);

    /**
     * @param appsDatumPinneds to remove
     */
    @Delete
    void deteleAppsData(AppsDataPinned... appsDatumPinneds);

    /**
     * @param appsDataPinned to update
     */
    @Update
    void updateAppsData(AppsDataPinned appsDataPinned);

    /**
     * @return list of AppsDataPinned
     */
    @Query("SELECT * FROM apps_data")
    List<AppsDataPinned> getAppsData();

    /**
     * @param app_package
     * @return AppsDataPinned
     */
    @Query("SELECT * FROM apps_data WHERE app_package = :app_package")
    AppsDataPinned getAppsDataById(String app_package);

    /**
     * @param app_title
     * @result Delete AppsDataPinned by id
     */
    @Query("DELETE FROM apps_data WHERE app_title =:app_title")
    void deleteAppsDataById(String app_title);

}