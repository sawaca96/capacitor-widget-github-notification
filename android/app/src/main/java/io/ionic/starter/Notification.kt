package io.ionic.starter

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.text.format.DateUtils

class Notification {
    var id: Int
    var title: String
    var repoName: String
    var updatedAt: String

    constructor(id: Int, title: String, repoName: String, updatedAt: String) {
        this.id = id
        this.title = title
        this.repoName = repoName
        this.updatedAt = updatedAt
    }

    fun timeDiff() : String {
        val updatedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(this.updatedAt)
        val now = Calendar.getInstance().time
        return DateUtils.getRelativeTimeSpanString(updatedAt.time, now.time, DateUtils.MINUTE_IN_MILLIS).toString()
    }
}