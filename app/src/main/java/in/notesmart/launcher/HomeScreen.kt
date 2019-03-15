package `in`.notesmart.launcher

import `in`.notesmart.launcher.Adapters.PinnedAdapter
import `in`.notesmart.launcher.DataBase.AppsDataDB
import `in`.notesmart.launcher.DataBase.AppsDataDao
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener
import com.github.pwittchen.swipe.library.rx2.Swipe
import java.util.*

class HomeScreen : AppCompatActivity(), PinnedAdapter.ItemClickListener {

    internal var runThread = false
    internal lateinit var appsDataDao: AppsDataDao
    private lateinit var manager: PackageManager
    private lateinit var adapter: PinnedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipe: Swipe
    internal lateinit var list: List<AppsDataPinned>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        recyclerView = findViewById(R.id.pinned_recyclerview)

        appsDataDao = AppsDataDB.getInstance(this).appsDataDao()
        manager = packageManager
        RunMyThread()

        swipe = Swipe()
        swipe.setListener(object : SimpleSwipeListener() {
            override fun onSwipedUp(event: MotionEvent?): Boolean {
                val intent = Intent(applicationContext, AppsDrawerActivity::class.java)
                startActivity(intent)
                return super.onSwipedUp(event)
            }
        })
    }

    override fun onBackPressed() {
        //DO NOTHING....
    }

    fun RunMyThread() {
        try {
            runThread = true
            val thread = AppsLoadingThread()
            Thread(thread).start()
        } catch (e: Exception) {

        }

    }

    fun StopMyThread() {
        runThread = false
    }

    override fun onResume() {
        super.onResume()
        RunMyThread()
    }

    override fun onPause() {
        super.onPause()
        StopMyThread()
    }

    override fun onDestroy() {
        super.onDestroy()
        StopMyThread()
    }

    override fun onItemClick(view: View, position: Int) {
        val intent = manager.getLaunchIntentForPackage(list[position].appPackage)
        startActivity(intent)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    fun SetAdaperts(dataPinneds: List<AppsDataPinned>) {
        recyclerView.layoutManager = GridLayoutManager(
                applicationContext, 5, LinearLayoutManager.VERTICAL, false)
        adapter = PinnedAdapter(applicationContext, dataPinneds)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
    }

    internal inner class AppsLoadingThread : Runnable {
        var threadHandler = Handler(Looper.getMainLooper())

        override fun run() {
            threadHandler.post {
                if (runThread) {
                    list = ArrayList(appsDataDao.appsData)
                    if (list.size < 4) {
                        val intent = Intent(Intent.ACTION_MAIN, null)
                        intent.addCategory(Intent.CATEGORY_LAUNCHER)
                        val availableActivities = manager.queryIntentActivities(intent, 0)
                        for (ri in availableActivities) {
                            val title = ri.loadLabel(manager).toString()
                            if (title.contains("Messaging") || title.contains("Messages") || title.contains("WhatsApp") ||
                                    title.contains("Phone") || title.contains("Chrome") || title.contains("YouTube") || title.contains("Message")) {
                                val data = AppsDataPinned()
                                data.appTitle = title
                                data.appPackage = ri.activityInfo.packageName
                                appsDataDao.insertAppsData(data)
                            }
                        }
                        list = ArrayList(appsDataDao.appsData)
                    }
                    SetAdaperts(list)
                }
            }
            Looper.prepare()
        }
    }
}
