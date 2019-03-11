package in.notesmart.launcher.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "apps_data")
public class AppsData implements Comparable<AppsData> {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "app_title")
    public String appTitle;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] appIcon;

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

    public byte[] getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(byte[] appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    @Override
    public int compareTo(AppsData o) {
        if (getAppTitle() == null || o.getAppTitle() == null) {
            return 0;
        }
        return getAppTitle().compareTo(o.getAppTitle());
    }
}
