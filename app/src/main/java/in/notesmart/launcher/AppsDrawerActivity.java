package in.notesmart.launcher;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.notesmart.launcher.Adapters.PremAdapter;
import in.notesmart.launcher.Model.AppsData;

public class AppsDrawerActivity extends AppCompatActivity implements PremAdapter.ItemClickListener, PremAdapter.ItemLongClickListner {

    boolean runThread = false;
    RecyclerView recyclerView;
    PremAdapter adapter;
    List<AppsData> data;
    PackageManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_drawer);
        recyclerView = findViewById(R.id.main_drawer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            runThread = true;
            MyThread thread = new MyThread();
            new Thread(thread).start();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        runThread = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runThread = false;
    }

    private void SetData() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        manager = getPackageManager();
        data = new ArrayList<>();
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo ri : availableActivities) {
            if (!ri.loadLabel(manager).toString().contains("Launcher")) {
                AppsData appsData = new AppsData();
                appsData.appTitle = ri.loadLabel(manager).toString();
                appsData.appPackage = ri.activityInfo.packageName;
                Drawable d = ri.loadIcon(manager);
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
                appsData.appIcon = stream.toByteArray();
                data.add(appsData);
            }
        }
        Collections.sort(data);
        adapter = new PremAdapter(getApplicationContext(), data, "main");
        recyclerView.setLayoutManager(new GridLayoutManager(
                getApplicationContext(), 5, LinearLayoutManager.VERTICAL, false));
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = manager.getLaunchIntentForPackage(data.get(position).appPackage);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        String[] options = {"App Info", "Uninstall"};
        final String packageName = data.get(position).getAppPackage();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent viewApp = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    viewApp.setData(Uri.parse("package:" + packageName));
                    startActivity(viewApp);
                } else if (which == 1) {
                    Intent removeApp = new Intent(Intent.ACTION_DELETE);
                    removeApp.setData(Uri.parse("package:" + packageName));
                    removeApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(removeApp);
                }
            }
        }).show();
    }

    class MyThread implements Runnable {

        Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void run() {
            if (runThread) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetData();
                    }
                });
            }
            Looper.prepare();
        }
    }
}