package com.castrec.stephane.androidcourseskotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.castrec.stephane.androidcourseskotlin.model.HttpResponse
import com.castrec.stephane.androidcourseskotlin.session.Session
import com.castrec.stephane.androidcourseskotlin.utils.APIRequestHelper
import com.castrec.stephane.androidcourseskotlin.utils.Constants
import com.castrec.stephane.androidcourseskotlin.utils.JSONParser
import com.castrec.stephane.androidcourseskotlin.utils.NetworkHelper


/**
 * Created by sca on 02/06/15.
 */
class SigninActivity : Activity() {

    internal lateinit var username: EditText
    internal lateinit var pwd: EditText
    internal lateinit var pg: ProgressBar
    internal lateinit var btn: Button
    internal lateinit var v: View

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_signin)
        v = findViewById(R.id.layout)
        username = findViewById(R.id.signin_username) as EditText
        pwd = findViewById(R.id.signin_pwd) as EditText
        pg = findViewById(R.id.signin_pg) as ProgressBar
        btn = findViewById(R.id.signin_btn) as Button
        btn.setOnClickListener { v ->
            loading(true)
            SigninAsyncTask(v.context).execute()
        }
        findViewById(R.id.signin_register).setOnClickListener { v ->
            val i = Intent(v.context, SignupActivity::class.java)
            startActivity(i)
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
    protected inner class SigninAsyncTask(internal var context: Context) : AsyncTask<Void, Void, HttpResponse>() {

        override fun doInBackground(vararg params: Void): HttpResponse? {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null
            }

            val map = mapOf("username" to username.text.toString(),
                    "pwd" to pwd.text.toString())

            return APIRequestHelper.doPost(context.getString(R.string.url_signin), map, null);
        }

        public override fun onPostExecute(rs: HttpResponse?) {
            loading(false)
            if (rs != null && rs.body != null) {
                Session.Companion.instance.token = JSONParser.getToken(rs.body)
                val i = Intent(context, DrawerActivity::class.java)
                i.putExtra(Constants.INTENT_TOKEN, JSONParser.getToken(rs.body))
                startActivity(i)
            } else {
                Snackbar.make(v,
                        context.getString(R.string.error_login),
                        Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
