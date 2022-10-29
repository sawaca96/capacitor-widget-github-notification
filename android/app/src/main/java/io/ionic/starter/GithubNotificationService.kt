package io.ionic.starter

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import kotlin.collections.ArrayList

class GithubNotificationService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubNotificationFactory(this.applicationContext, intent)
    }
}

class GithubNotificationFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {
    private val notifications: MutableList<Notification> = ArrayList()

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

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

    override fun getCount(): Int {
        return notifications.count()
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.github_notification_item)
        rv.setTextViewText(R.id.widgetItemTitle, notifications[p0].title)
        rv.setTextViewText(R.id.widgetRepoName, notifications[p0].repoName)
        rv.setTextViewText(R.id.widgetItemUpdatedAt, notifications[p0].timeDiff())
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
