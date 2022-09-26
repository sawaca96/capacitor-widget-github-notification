package io.ionic.starter

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat


// create GithubPullRequestConfigure for widget configuration
class GithubPullRequestConfig : AppCompatActivity() {
    private var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var allCheckBox: CheckBox? = null
    private var participatingCheckBox: CheckBox? = null
//    private var sinceEditText: EditText? = null
//    private var beforeEditText: EditText? = null
    private var pageEditText: EditText? = null
    private var perPageEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the view layout resource to use.
        setContentView(R.layout.github_pull_request_config)

        allCheckBox = findViewById<View>(R.id.all_params) as CheckBox
        participatingCheckBox = findViewById<View>(R.id.participating_params) as CheckBox
//        sinceEditText = findViewById<View>(R.id.since_params) as EditText
//        beforeEditText = findViewById<View>(R.id.before_params) as EditText
        pageEditText = findViewById<View>(R.id.page_params) as EditText
        perPageEditText = findViewById<View>(R.id.per_page_params) as EditText

        val prefs = this.getSharedPreferences("github_pull_request", Context.MODE_PRIVATE)
        val all= prefs.getString("all", false.toString())
        val participating = prefs.getString("participating", false.toString())
        val since = prefs.getString("since", null)
        val before = prefs.getString("before", null)
        val page = prefs.getString("page", 1.toString())
        val perPage = prefs.getString("perPage", 50.toString())

        // set default value
        allCheckBox!!.isChecked = all!!.toBoolean()
        participatingCheckBox!!.isChecked = participating!!.toBoolean()
//        sinceEditText!!.setText(since)
//        beforeEditText!!.setText(before)
        pageEditText!!.setText(page)
        perPageEditText!!.setText(perPage)




        // Find the widget id from the intent.
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }


    }

    fun confirmConfiguration(view: View) {
        val all = allCheckBox!!.isChecked.toString()
        val participating = participatingCheckBox!!.isChecked.toString()
//        val since = sinceEditText!!.text.toString()
//        val before = beforeEditText!!.text.toString()
        val page = pageEditText!!.text.toString()
        val perPage = perPageEditText!!.text.toString()

//        try {
//            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//            sdf.parse(since)
//            sdf.parse(before)
//        } catch (e: Exception) {
//            Toast.makeText(this, "format must be YYYY-MM-DDTHH:MM:SSZ", Toast.LENGTH_SHORT).show()
//            return
//        }
        // check page is positive integer
        if (page.toInt() < 1) {
            Toast.makeText(this, "page must be positive integer", Toast.LENGTH_SHORT).show()
            return
        }
        // check perPage is positive integer and max is 50
        if (perPage.toInt() < 1 || perPage.toInt() > 50) {
            Toast.makeText(this, "perPage must be positive integer and max is 50", Toast.LENGTH_SHORT).show()
            return
        }
        // save all, participating, since, before, page, perPage to shared preferences
        val sharedPreferences = getSharedPreferences("github_pull_request", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("all", all)
        editor.putString("participating", participating)
//        editor.putString("since", since)
//        editor.putString("before", before)
        editor.putString("page", page)
        editor.putString("perPage", perPage)
        editor.apply()


//        val appWidgetManager = AppWidgetManager.getInstance(this)
//        val views = RemoteViews(this.packageName, R.layout.github_pull_request)
//        appWidgetManager.updateAppWidget(mAppWidgetId, views)

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }
    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

}


