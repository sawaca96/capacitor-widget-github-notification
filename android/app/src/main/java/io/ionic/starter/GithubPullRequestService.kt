package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.whitestein.securestorage.PasswordStorageHelper
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


class GithubPullRequestService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubPullRequestFactory(this.applicationContext, intent)
    }
}

class GithubPullRequestFactory(private val widgetContext: Context, val intent: Intent) :
    RemoteViewsFactory {
    private var notifications: MutableList<HashMap<String, String>> = ArrayList()

    override fun onCreate() {}

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

        rv.setTextViewText(R.id.widgetItemTitle, notifications[index]["title"])
        rv.setTextViewText(R.id.widgetRepoName, notifications[index]["repoName"])
        rv.setTextViewText(R.id.widgetItemUpdatedAt, notifications[index]["updatedAt"])

        when (notifications[index]["type"]) {
            "CheckSuite" -> rv.setImageViewResource(R.id.widgetItemIcon, R.drawable.ic_octicon_sync)
            "Commit" -> rv.setImageViewResource(
                R.id.widgetItemIcon,
                R.drawable.ic_octicon_git_commit
            )
            "Discussion" -> rv.setImageViewResource(
                R.id.widgetItemIcon,
                R.drawable.ic_octicon_comment_discussion
            )
            "Issue" -> rv.setImageViewResource(
                R.id.widgetItemIcon,
                R.drawable.ic_octicon_issue_opened
            )
            "PullRequest" -> rv.setImageViewResource(
                R.id.widgetItemIcon,
                R.drawable.ic_octicon_git_pull_request
            )
            "Release" -> rv.setImageViewResource(R.id.widgetItemIcon, R.drawable.ic_octicon_tag)
            "RepositoryVulnerabilityAlert" -> rv.setImageViewResource(
                R.id.widgetItemIcon,
                R.drawable.ic_octicon_alert
            )
            else -> rv.setImageViewResource(R.id.widgetItemIcon, R.drawable.ic_octicon_question)
        }

        val fillInIntent = Intent()
        fillInIntent.putExtra("url", notifications[index]["url"])
        rv.setOnClickFillInIntent(R.id.widgetItem, fillInIntent)

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

        val prefs = widgetContext.getSharedPreferences("github_pull_request", Context.MODE_PRIVATE)
        val all= prefs.getString("all", false.toString())
        val participating = prefs.getString("participating", false.toString())
        val since = prefs.getString("since", null)
        val before = prefs.getString("before", null)
        val page = prefs.getString("page", 1.toString())
        val perPage = prefs.getString("perPage", 50.toString())

        // add query params to url except null values
        val query = "all=$all&participating=$participating&page=$page&per_page=$perPage" +
                if (since != null) "&since=$since" else "" +
                if (before != null) "&before=$before" else ""
        val url = URL("https://api.github.com/notifications?$query")

        val result = httpGet(url)
        for (i in 0 until result.length()) {
            val jsonObject = result.getJSONObject(i)
            val updatedAt = jsonObject.getString("updated_at")
            val repository = jsonObject.getJSONObject("repository")
            val repositoryName = repository.getString("name")
            val repositoryFullName = repository.getString("full_name")
            val subject = jsonObject.getJSONObject("subject")
            val title = subject.getString("title")
            val type = subject.getString("type")
            val rawUrl = subject.getString("url")

            val pullRequestId = rawUrl.substring(rawUrl.lastIndexOf("/") + 1)
            val url = "https://github.com/$repositoryFullName/pull/$pullRequestId"
            val map = HashMap<String, String>()
            map["title"] = title
            map["repoName"] = repositoryName
            map["type"] = type
            map["url"] = url
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            val date = inputFormat.parse(updatedAt)
            val timeDiff = DateUtils.getRelativeTimeSpanString(
                date.time,
                Calendar.getInstance().timeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            );
            map["updatedAt"] = timeDiff.toString()
            notifications.add(map)
        }
    }

    private fun httpGet(
        url: URL
    ): JSONArray {
        // Move the execution of the coroutine to the I/O dispatcher
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Authorization", "token ${getToken()}")
        conn.setRequestProperty(
            "Content-Type",
            "application/json"
        ) // The format of the content we're sending to the server
        conn.setRequestProperty("Accept", "application/json") //
        // add parameter

        return if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            val buffered = conn.inputStream.bufferedReader().use { it.readText() }
            JSONArray(buffered)
        } else {
            JSONArray()
        }
    }

    fun getToken(): String {
        val storageHelper = PasswordStorageHelper(widgetContext)
        return try {
            val buffer: ByteArray = storageHelper.getData("githubToken")
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            ""
        }
    }
}

