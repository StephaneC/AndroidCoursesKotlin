package com.castrec.stephane.androidcourseskotlin

/**
 * Created by sca on 08/08/17.
 */

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.castrec.stephane.androidcourseskotlin.model.HttpResponse
import com.castrec.stephane.androidcourseskotlin.utils.APIRequestHelper


import com.castrec.stephane.androidcourseskotlin.utils.NetworkHelper
import java.io.InputStream
import java.net.HttpURLConnection

import java.net.URL

/**
 * Created by sca on 02/06/15.
 */
class SignupActivity : Activity() {

    private val TAG: String? = "SignupActivity"

    internal lateinit var username: EditText
    internal lateinit var pwd: EditText
    internal lateinit var url: EditText
    internal lateinit var pg: ProgressBar
    internal lateinit var btn: Button

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_signup)
        username = findViewById(R.id.signup_username) as EditText
        pwd = findViewById(R.id.signup_pwd) as EditText
        pg = findViewById(R.id.signup_pg) as ProgressBar
        url = findViewById(R.id.signup_url) as EditText

        btn = findViewById(R.id.signup_btn) as Button
        btn.setOnClickListener { v ->
            loading(true)
            SignupAsyncTask(v.context).execute()
        }
    }

    private fun loading(loading: Boolean) {
        if (loading) {
            pg.visibility = View.VISIBLE
            btn.visibility = View.INVISIBLE
        } else {
            pg.visibility = View.INVISIBLE
            btn.visibility = View.VISIBLE
        }
    }

    /**
     * AsyncTask for sign-in
     */
    protected inner class SignupAsyncTask(internal var context: Context) : AsyncTask<Void, Void, HttpResponse>() {

        override fun doInBackground(vararg params: Void): HttpResponse? {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return HttpResponse(body =null, status =500)
            }

            val map = mapOf("username" to username.text.toString(),
                    "pwd" to pwd.text.toString(),
                    "urlPhoto" to url.text.toString())

            return APIRequestHelper.doPost(context.getString(R.string.url_signup), map, null)
        }

        public override fun onPostExecute(rs: HttpResponse?) {
            loading(false)
            if (rs != null && rs.body != null) {
                this@SignupActivity.finish()
            } else {
                Toast.makeText(context, context.getString(R.string.error_signup), Toast.LENGTH_LONG).show()
            }
        }
    }
}