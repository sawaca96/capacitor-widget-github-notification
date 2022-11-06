package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class GithubNotificationService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubNotificationFactory(this.applicationContext, intent)
    }
}

class GithubNotificationFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {
    private val notifications: ArrayList<Notification> = ArrayList()

    override fun onCreate() {
        notifications.add(
            Notification(
                1,
                "New issue opened",
                "ionic-team/ionic",
                "2022-10-29T08:49:35Z"
            )
        )
        notifications.add(
            Notification(
                2,
                "New issue opened",
                "ionic-team/ionic",
                "2022-10-29T14:49:35Z"
            )
        )
        notifications.add(
            Notification(
                3,
                "Deploy failed",
                "alphaprime-dev/alphasquare",
                "2022-10-28T08:49:35Z"
            )
        )
        notifications.add(
            Notification(
                4,
                "New issue opened",
                "ionic-team/ionic",
                "2022-09-28T08:49:35Z"
            )
        )
        notifications.add(
            Notification(
                5,
                "Something happened",
                "facebook-create-react-app",
                "2022-10-10T08:49:35Z"
            )
        )
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

        Log.e("onDataSetChanged", "onDataSetChanged")
        // TODO: fetch data from github
        SystemClock.sleep(2000);

        view.setViewVisibility(R.id.widgetSyncButtonRotate, View.GONE)
        view.setViewVisibility(R.id.widgetSyncButton, View.VISIBLE)
        appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, view)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return notifications.size
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.github_notification_item)
        rv.setTextViewText(R.id.widgetNotificationTitle, notifications[p0].title)
        rv.setTextViewText(R.id.widgetNotificationRepoName, notifications[p0].repoName)
        rv.setTextViewText(R.id.widgetNotificationUpdatedAt, notifications[p0].timeDiff())
        return rv
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

}