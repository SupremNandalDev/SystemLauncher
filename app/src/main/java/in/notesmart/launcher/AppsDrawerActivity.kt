package `in`.notesmart.launcher

import `in`.notesmart.launcher.Adapters.DrawerAdapter
import `in`.notesmart.launcher.Model.AppsData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.*

class AppsDrawerActivity : AppCompatActivity(), DrawerAdapter.ItemClickListener, DrawerAdapter.ItemLongClickListner {

    internal var runThread = false
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var adapter: DrawerAdapter
    internal lateinit var data: MutableList<AppsData>
    internal lateinit var manager: PackageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.apps_drawer)
        recyclerView = findViewById(R.id.main_drawer)
    }

    override fun onResume() {
        super.onResume()
        try {
            runThread = true
            val thread = MyThread()
            Thread(thread).start()
        } catch (e: Exception) {

        }

    }

    override fun onPause() {
        super.onPause()
        runThread = false
    }

    override fun onDestroy() {
        super.onDestroy()
        runThread = false
    }

    private fun SetData() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        manager = packageManager
        data = ArrayList()
        val availableActivities = manager.queryIntentActivities(intent, 0)
        for (ri in availableActivities) {
            if (!ri.loadLabel(manager).toString().contains("Launcher")) {
                val appsData = AppsData()
                appsData.appTitle = ri.loadLabel(manager).toString()
                appsData.appPackage = ri.activityInfo.packageName
                appsData.appIcon = ri.loadIcon(manager)
                data.add(appsData)
            }
        }
        Collections.sort(data)
        adapter = DrawerAdapter(applicationContext, data)
        recyclerView.layoutManager = GridLayoutManager(
                applicationContext, 5, LinearLayoutManager.VERTICAL, false)
        adapter.setClickListener(this)
        adapter.setLongClickListener(this)
        recyclerView.adapter = adapter

    }

    override fun onItemClick(view: View, position: Int) {
        val intent = manager.getLaunchIntentForPackage(data[position].appPackage)
        startActivity(intent)
    }

    override fun onItemLongClick(view: View, position: Int) {
        val options = arrayOf("App Info", "Uninstall")
        val packageName = data[position].getAppPackage()
        val builder = AlertDialog.Builder(this)
        builder.setItems(options) { dialog, which ->
            if (which == 0) {
                val viewApp = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                viewApp.data = Uri.parse("package:$packageName")
                startActivity(viewApp)
            } else if (which == 1) {
                val removeApp = Intent(Intent.ACTION_DELETE)
                removeApp.data = Uri.parse("package:$packageName")
                removeApp.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(removeApp)
            }
        }.show()
    }

    internal inner class MyThread : Runnable {

        var handler = Handler(Looper.getMainLooper())

        override fun run() {
            if (runThread) {
                handler.post { SetData() }
            }
            Looper.prepare()
        }
    }
}