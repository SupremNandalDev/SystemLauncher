package in.notesmart.launcher.Model;

import android.graphics.drawable.Drawable;

public class AppsData implements Comparable<AppsData> {

    public String appTitle;
    public Drawable appIcon;
    public String appPackage;

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
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
