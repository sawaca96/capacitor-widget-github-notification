package io.ionic.starter

import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class GithubClient(private var token: String) {

    private fun httpGet(
        url: URL
    ): JSONArray {
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Authorization", "token ${this.token}")
        conn.setRequestProperty(
            "Content-Type",
            "application/json"
        )
        conn.setRequestProperty("Accept", "application/json") //

        return if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            val buffered = conn.inputStream.bufferedReader().use { it.readText() }
            JSONArray(buffered)
        } else {
            JSONArray()
        }
    }


    fun fetchNotifications(): ArrayList<Notification> {
        val notifications: ArrayList<Notification> = ArrayList()
        val url = URL("https://api.github.com/notifications")
        val response = httpGet(url)
        for (i in 0 until response.length()) {
            val jsonObject = response.getJSONObject(i)
            val repository = jsonObject.getJSONObject("repository")
            val subject = jsonObject.getJSONObject("subject")
            val rawUrl = subject.getString("url")

            val title = subject.getString("title")
            val repositoryName = repository.getString("name")
            val repositoryFullName = repository.getString("full_name")
            val updatedAt = jsonObject.getString("updated_at")
            val type = subject.getString("type")
            val pullRequestId = rawUrl.substring(rawUrl.lastIndexOf("/") + 1).toIntOrNull()

            notifications.add(
                Notification(
                    i,
                    title,
                    repositoryName,
                    repositoryFullName,
                    updatedAt,
                    type,
                    pullRequestId
                )
            )
        }
        return notifications
    }


}