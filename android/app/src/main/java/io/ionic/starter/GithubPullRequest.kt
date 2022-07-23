package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class GithubPullRequest : AppWidgetProvider() {
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

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.github_pull_request)

    // Set widget updated at
    val dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())
    views.setTextViewText(R.id.widgetUpdatedAt,
        context.resources.getString(
            R.string.app_widget_updated_at, dateString));

    // Set widget list
    val serviceIntent = Intent(context, GithubPullRequestService::class.java)
    views.setRemoteAdapter(R.id.widgetList, serviceIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
