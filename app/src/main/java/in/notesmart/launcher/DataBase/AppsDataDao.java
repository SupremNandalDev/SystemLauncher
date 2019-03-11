package in.notesmart.launcher.DataBase;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import in.notesmart.launcher.Model.AppsData;

@Dao
public interface AppsDataDao {

    /**
     * @param appsData
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertAppsData(AppsData appsData);

    /**
     * @param appsData to remove
     */
    @Delete
    void deteleAppsData(AppsData... appsData);

    /**
     * @param appsData to update
     */
    @Update
    void updateAppsData(AppsData appsData);

    /**
     * @return list of AppsData
     */
    @Query("SELECT * FROM apps_data")
    List<AppsData> getAppsData();

    /**
     * @param app_package
     * @return AppsData
     */
    @Query("SELECT * FROM apps_data WHERE app_package = :app_package")
    AppsData getAppsDataById(String app_package);

    /**
     * @param app_title
     * @result Delete AppsData by id
     */
    @Query("DELETE FROM apps_data WHERE app_title =:app_title")
    void deleteAppsDataById(String app_title);

}