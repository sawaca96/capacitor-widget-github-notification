/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ionic.starter

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*


class GithubPullRequestProvider : AppWidgetProvider() {
    val ITEM_CLICKED = "ITEM_CLICKED"
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ITEM_CLICKED) {
            val url = intent.getStringExtra("url")
            val openIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(openIntent)
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val rv = RemoteViews(context.packageName, R.layout.github_pull_request)
            val dateString = SimpleDateFormat("H:mm", Locale.getDefault()).format(Date())
            rv.setTextViewText(
                R.id.widgetUpdatedAt,
                context.resources.getString(
                    R.string.app_widget_updated_at, dateString
                )
            );

            val serviceIntent = Intent(context, GithubPullRequestService::class.java)
            rv.setRemoteAdapter(R.id.widgetList, serviceIntent)
            rv.setEmptyView(R.id.widgetList, R.id.empty_view)

            val syncIntent = Intent(context, GithubPullRequestProvider::class.java)
            syncIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            syncIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            val syncPendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                syncIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            rv.setOnClickPendingIntent(R.id.widgetSyncIcon, syncPendingIntent)

            val clickIntent = Intent(context, GithubPullRequestProvider::class.java)
            clickIntent.action = ITEM_CLICKED
            val clickPendingIntent = PendingIntent.getBroadcast(
                context, appWidgetId, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            rv.setPendingIntentTemplate(R.id.widgetList, clickPendingIntent)

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetList)
            appWidgetManager.updateAppWidget(appWidgetId, rv)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

}