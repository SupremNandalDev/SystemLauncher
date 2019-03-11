package in.notesmart.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import in.notesmart.launcher.Adapters.PremAdapter;
import in.notesmart.launcher.DataBase.AppsDataDB;
import in.notesmart.launcher.DataBase.AppsDataDao;
import in.notesmart.launcher.Model.AppsData;

public class HomeScreen extends AppCompatActivity implements PremAdapter.ItemClickListener {

    boolean runThread = false;
    AppsDataDao appsDataDao;
    List<AppsData> tempData;
    PackageManager manager;
    PremAdapter adapter;
    RecyclerView recyclerView;
    Swipe swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        recyclerView = findViewById(R.id.pinned_recyclerview);

        appsDataDao = AppsDataDB.getInstance(this).appsDataDao();
        manager = getPackageManager();
        RunMyThread();

        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {
            @Override
            public boolean onSwipedUp(MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), AppsDrawerActivity.class);
                startActivity(intent);
                return super.onSwipedUp(event);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //DO NOTHING....
    }

    public void RunMyThread() {
        try {
            runThread = true;
            AppsLoadingThread thread = new AppsLoadingThread();
            new Thread(thread).start();
        } catch (Exception e) {

        }
    }

    public void StopMyThread() {
        runThread = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RunMyThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StopMyThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopMyThread();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = manager.getLaunchIntentForPackage(tempData.get(position).appPackage);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    public void SetAdaperts() {
        recyclerView.setLayoutManager(new GridLayoutManager(
                getApplicationContext(), 5, LinearLayoutManager.VERTICAL, false));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    class AppsLoadingThread implements Runnable {
        Handler threadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void run() {
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (runThread) {
                        Intent intent = new Intent(Intent.ACTION_MAIN, null);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
                        tempData = new ArrayList<>(appsDataDao.getAppsData());
                        if (tempData.isEmpty()) {
                            for (ResolveInfo ri : availableActivities) {
                                String title = ri.loadLabel(manager).toString();
                                if (title.contains("Messaging") || title.contains("Messages") || title.contains("WhatsApp") ||
                                        title.contains("Phone") || title.contains("Chrome") || title.contains("YouTube") || title.contains("Message")) {
                                    AppsData data = new AppsData();
                                    data.appTitle = title;
                                    data.appPackage = ri.activityInfo.packageName;
                                    Drawable d = ri.loadIcon(manager);
                                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
                                    data.appIcon = stream.toByteArray();
                                    appsDataDao.insertAppsData(data);
                                }
                            }
                            tempData = new ArrayList<>(appsDataDao.getAppsData());
                        }
                        adapter = new PremAdapter(getApplicationContext(), tempData, "pinned");
                        SetAdaperts();
                    }
                }
            });
            Looper.prepare();
        }
    }


    /*private void LongItemEvent(String[] init, final String srt, final AppsData data){
        final String packageName = data.getAppPackage();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(init, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        if (srt.contains("main")) {
                            pinData.add(data);
                            appsDataDao.insertAppsData(data);
                        } else if (srt.contains("pinned")) {
                            pinData.remove(data);
                            appsDataDao.deteleAppsData(data);
                        }
                        return;
                    case 1:
                        Toast.makeText(HomeScreen.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        return;
                    case 2:
                        Intent viewApp = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        viewApp.setData(Uri.parse("package:" + packageName));
                        startActivity(viewApp);
                        return;
                    case 3:
                        Intent removeApp = new Intent(Intent.ACTION_DELETE);
                        removeApp.setData(Uri.parse("package:" + packageName));
                        removeApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(removeApp);
                        RunMyThread();
                        return;
                }
            }
        }).show();
    }*/
}
