package in.notesmart.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import java.util.ArrayList;
import java.util.List;

import in.notesmart.launcher.Adapters.PinnedAdapter;
import in.notesmart.launcher.DataBase.AppsDataDB;
import in.notesmart.launcher.DataBase.AppsDataDao;
import in.notesmart.launcher.Model.AppsDataPinned;

public class HomeScreen extends AppCompatActivity implements PinnedAdapter.ItemClickListener {

    boolean runThread = false;
    AppsDataDao appsDataDao;
    PackageManager manager;
    PinnedAdapter adapter;
    RecyclerView recyclerView;
    Swipe swipe;
    List<AppsDataPinned> list;

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
        Intent intent = manager.getLaunchIntentForPackage(list.get(position).getAppPackage());
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    public void SetAdaperts(List<AppsDataPinned> dataPinneds) {
        recyclerView.setLayoutManager(new GridLayoutManager(
                getApplicationContext(), 5, LinearLayoutManager.VERTICAL, false));
        adapter = new PinnedAdapter(getApplicationContext(), dataPinneds);
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
                        list = new ArrayList<>(appsDataDao.getAppsData());
                        if (list.size() < 4) {
                            Intent intent = new Intent(Intent.ACTION_MAIN, null);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
                            for (ResolveInfo ri : availableActivities) {
                                String title = ri.loadLabel(manager).toString();
                                if (title.contains("Messaging") || title.contains("Messages") || title.contains("WhatsApp") ||
                                        title.contains("Phone") || title.contains("Chrome") || title.contains("YouTube") || title.contains("Message")) {
                                    AppsDataPinned data = new AppsDataPinned();
                                    data.appTitle = title;
                                    data.appPackage = ri.activityInfo.packageName;
                                    appsDataDao.insertAppsData(data);
                                }
                            }
                            list = new ArrayList<>(appsDataDao.getAppsData());
                        }
                        SetAdaperts(list);
                    }
                }
            });
            Looper.prepare();
        }
    }
}
