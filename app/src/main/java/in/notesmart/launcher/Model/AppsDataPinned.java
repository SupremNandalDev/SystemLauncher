package in.notesmart.launcher.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "apps_data")
public class AppsDataPinned implements Comparable<AppsDataPinned> {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "app_title")
    public String appTitle;

    @ColumnInfo(name = "app_package")
    public String appPackage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    @Override
    public int compareTo(AppsDataPinned o) {
        if (getAppTitle() == null || o.getAppTitle() == null) {
            return 0;
        }
        return getAppTitle().compareTo(o.getAppTitle());
    }
}