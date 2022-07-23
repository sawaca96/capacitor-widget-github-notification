package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class GithubPullRequestService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubPullRequestFactory(this.applicationContext, intent)
    }
}

internal class GithubPullRequestFactory(private val mContext: Context, intent: Intent) :
    RemoteViewsFactory {
    private val mWidgetItems: MutableList<WidgetItem> = ArrayList()
    private val mAppWidgetId: Int

    override fun onCreate() {
        GlobalScope.launch {
            val result = httpGet("https://api.github.com/notifications?all=true")
            mCount = result.length()
            for (i in 0 until mCount) {
                val jsonObject = result.getJSONObject(i)
                val subject = jsonObject.getJSONObject("subject")
                val title = subject.getString("title")
                mWidgetItems.add(WidgetItem(title))
            }

        }
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

        // for (i in 0 until mCount) {
        //     mWidgetItems.add(WidgetItem("$i!"))
        // }

        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear()
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getViewAt(position: Int): RemoteViews {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        val rv = RemoteViews(mContext.packageName, R.layout.pull_request_item)
        rv.setTextViewText(R.id.pull_request_item, mWidgetItems[position].text)
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        val extras = Bundle()
        extras.putInt(GithubPullRequestProvider.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.pull_request_item, fillInIntent)
        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
        try {
            println("Loading view $position")
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        // Return the remote views object.
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }

    companion object {
        private var mCount = 0
    }

    init {
        mAppWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }


    private suspend fun httpGet(
        jsonBody: String
    ):JSONArray {
        // Move the execution of the coroutine to the I/O dispatcher
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.github.com/notifications")
            val token = ""

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "token $token")
            conn.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server
            conn.setRequestProperty("Accept", "application/json") //

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val streamReader = InputStreamReader(conn.inputStream)
                val buffered = conn.inputStream.bufferedReader().use { it.readText() }
                return@withContext JSONArray(buffered)
            } else {
                Log.e("GithubPullRequest", "Error: " + conn.responseCode)
                return@withContext JSONArray()
            }
        }
    }
}