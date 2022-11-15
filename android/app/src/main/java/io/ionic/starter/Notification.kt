package io.ionic.starter

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.text.format.DateUtils

class Notification {
    var id: Int
    var title: String
    var updatedAt: String
    var repoName: String
    var repoFullName: String
    var type: String
    var pullRequestId: Int?


    constructor(
        id: Int,
        title: String,
        repoName: String,
        repoFullName: String,
        updatedAt: String,
        type: String,
        pullRequestId: Int?
    ) {
        this.id = id
        this.title = title
        this.repoName = repoName
        this.repoFullName = repoFullName
        this.updatedAt = updatedAt
        this.type = type
        this.pullRequestId = pullRequestId
    }

    fun timeDiff(): String {
        val updatedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(this.updatedAt)
        val now = Calendar.getInstance().time
        return DateUtils.getRelativeTimeSpanString(
            updatedAt.time,
            now.time,
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    fun url(): String {
        return "https://github.com/$repoFullName/pull/$pullRequestId"
    }
}