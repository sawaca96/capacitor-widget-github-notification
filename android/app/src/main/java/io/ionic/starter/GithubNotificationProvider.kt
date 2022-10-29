package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.widget.RemoteViews
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
            val rv = RemoteViews(context.packageName, R.layout.github_notification)

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
        SimpleDateFormat("H:mm").format(Date()).toString()
    )

    val serviceIntent = Intent(context, GithubNotificationService::class.java)
    views.setRemoteAdapter(R.id.widgetItemList, serviceIntent)
    views.setEmptyView(R.id.widgetItemList, R.id.widgetEmptyText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}