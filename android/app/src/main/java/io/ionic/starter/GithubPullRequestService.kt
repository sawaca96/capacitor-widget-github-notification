package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL


class GithubPullRequestService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubPullRequestFactory(this.applicationContext, intent)
    }
}

class GithubPullRequestFactory(private val widgetContext: Context, intent: Intent) :
    RemoteViewsFactory {
    private val notifications: MutableList<Notification> = ArrayList()
    private val token: String = BuildConfig.GITHUB_TOKEN

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val appWidgetManager = AppWidgetManager.getInstance(widgetContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                widgetContext,
                GithubPullRequestProvider::class.java
            )
        )
        val view = RemoteViews(widgetContext.packageName, R.layout.github_pull_request)

        view.setViewVisibility(R.id.widgetSyncIcon, View.GONE)
        view.setViewVisibility(R.id.widgetSyncIconRotate, View.VISIBLE)
        appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, view)

        fetchNotifications()

        view.setViewVisibility(R.id.widgetSyncIconRotate, View.GONE)
        view.setViewVisibility(R.id.widgetSyncIcon, View.VISIBLE)
        appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, view)
    }

    override fun onDestroy() {
        notifications.clear()
    }

    override fun getCount(): Int {
        return notifications.count()
    }

    override fun getViewAt(index: Int): RemoteViews {
        val rv = RemoteViews(widgetContext.packageName, R.layout.pull_request_item)
        rv.setTextViewText(R.id.pull_request_item, notifications[index].text)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun fetchNotifications() {
        notifications.clear()
        val result = httpGet(URL("https://api.github.com/notifications"))
        for (i in 0 until result.length()) {
            val jsonObject = result.getJSONObject(i)
            val subject = jsonObject.getJSONObject("subject")
            val title = subject.getString("title")
            notifications.add(Notification(title))
        }
    }

    private fun httpGet(
        url: URL
    ): JSONArray {
        // Move the execution of the coroutine to the I/O dispatcher
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Authorization", "token $token")
        conn.setRequestProperty(
            "Content-Type",
            "application/json"
        ) // The format of the content we're sending to the server
        conn.setRequestProperty("Accept", "application/json") //

        return if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            val buffered = conn.inputStream.bufferedReader().use { it.readText() }
            JSONArray(buffered)
        } else {
            JSONArray()
        }
    }

}

