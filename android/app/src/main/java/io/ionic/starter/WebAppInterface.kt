package io.ionic.starter


import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast

class WebAppInterface(val mainActivity: MainActivity, private val webView: WebView) {
    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
        webView.post {
            try {
                webView.loadUrl("javascript:alertMessage('From Native');")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}