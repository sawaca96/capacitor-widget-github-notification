package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.whitestein.securestorage.PasswordStorageHelper
import java.nio.charset.Charset


class GithubNotificationService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubNotificationFactory(this.applicationContext, intent)
    }
}

class GithubNotificationFactory(
    private val context: Context,
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {
    private var notifications: ArrayList<Notification> = ArrayList()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                context,
                GithubNotificationProvider::class.java
            )
        )
        val view = RemoteViews(context.packageName, R.layout.github_notification)

        view.setViewVisibility(R.id.widgetSyncButton, View.GONE)
        view.setViewVisibility(R.id.widgetSyncButtonRotate, View.VISIBLE)
        appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, view)

        notifications = GithubClient(this.getToken()).fetchNotifications()

        view.setViewVisibility(R.id.widgetSyncButtonRotate, View.GONE)
        view.setViewVisibility(R.id.widgetSyncButton, View.VISIBLE)
        appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, view)
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return notifications.size
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.github_notification_item)
        rv.setTextViewText(R.id.widgetNotificationTitle, notifications[p0].title)
        rv.setTextViewText(R.id.widgetNotificationRepoName, notifications[p0].repoName)
        rv.setTextViewText(R.id.widgetNotificationUpdatedAt, notifications[p0].timeDiff())
        setIcon(notifications[p0].type, rv)

        intent.putExtra("url", notifications[p0].url())
        intent.action = ITEM_CLICKED
        rv.setOnClickFillInIntent(R.id.widgetNotification, intent)
        return rv
    }

    private fun setIcon(type: String, rv: RemoteViews) {
        when (type) {
            "CheckSuite" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_sync
            )
            "Commit" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_git_commit
            )
            "Discussion" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_comment_discussion
            )
            "Issue" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_issue_opened
            )
            "PullRequest" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_git_pull_request
            )
            "Release" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_tag
            )
            "RepositoryVulnerabilityAlert" -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_alert
            )
            else -> rv.setImageViewResource(
                R.id.widgetNotificationIcon,
                R.drawable.ic_octicon_question
            )
        }
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.github_notification_item)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun getToken(): String {
        val storageHelper = PasswordStorageHelper(this.context)
        return try {
            val buffer: ByteArray = storageHelper.getData("githubToken")
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            ""
        }
    }

}