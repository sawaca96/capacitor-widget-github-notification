package io.ionic.starter

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [GithubNotificationConfigureActivity]
 */
class GithubNotificationProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.github_notification)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    views.setTextViewText(
        R.id.widgetUpdatedAt,
        "마지막 업데이트: ${SimpleDateFormat("H:mm").format(Date())}"
    )

    val serviceIntent = Intent(context, GithubNotificationService::class.java)
    views.setRemoteAdapter(R.id.widgetListView, serviceIntent)
    views.setEmptyView(R.id.widgetListView, R.id.widgetEmptyList)

    val syncIntent = Intent(context, GithubNotificationProvider::class.java)
    syncIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    val componentName =
        ComponentName(context.packageName, GithubNotificationProvider::class.java.name)
    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
    syncIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    val syncPendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        syncIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.widgetSyncButton, syncPendingIntent)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}