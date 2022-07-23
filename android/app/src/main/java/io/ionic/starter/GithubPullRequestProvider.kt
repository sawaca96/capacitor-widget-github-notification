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
import android.widget.Toast

class GithubPullRequestProvider : AppWidgetProvider() {
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
        val mgr = AppWidgetManager.getInstance(context)
        if (intent.action == TOAST_ACTION) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
            Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // update each of the widgets with the remote adapter
        for (i in appWidgetIds.indices) {
            // Here we setup the intent which points to the StackViewService which will
            // provide the views for this collection.
            val intent = Intent(context, GithubPullRequestService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val rv = RemoteViews(context.packageName, R.layout.github_pull_request)
            rv.setRemoteAdapter(R.id.widgetList, intent)
            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            rv.setEmptyView(R.id.widgetList, R.id.empty_view)
            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            val toastIntent = Intent(context, GithubPullRequestProvider::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPendingIntent = PendingIntent.getBroadcast(
                context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            rv.setPendingIntentTemplate(R.id.widgetList, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    companion object {
        const val TOAST_ACTION = "io.ionic.starter.TOAST_ACTION"
        const val EXTRA_ITEM = "io.ionic.starter.EXTRA_ITEM"
    }
}