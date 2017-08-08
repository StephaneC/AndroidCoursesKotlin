package com.castrec.stephane.androidcourseskotlin.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.castrec.stephane.androidcourseskotlin.R
import com.castrec.stephane.androidcourseskotlin.adapter.UserAdapter
import com.castrec.stephane.androidcourseskotlin.model.HttpResponse
import com.castrec.stephane.androidcourseskotlin.session.Session
import com.castrec.stephane.androidcourseskotlin.utils.APIRequestHelper
import com.castrec.stephane.androidcourseskotlin.utils.JSONParser
import com.castrec.stephane.androidcourseskotlin.utils.NetworkHelper
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by sca on 06/06/15.
 */
class UsersFragment : Fragment() {

    //UI
    internal lateinit var swipeLayout: SwipeRefreshLayout
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var adapter: UserAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater!!.inflate(
                R.layout.fragment_users, container, false)
        recyclerView = v.findViewById(R.id.users_list) as RecyclerView
        swipeLayout = v.findViewById(R.id.users_swiperefresh) as SwipeRefreshLayout
        setupRefreshLayout()
        setupRecyclerView()
        return v
    }

    override fun onResume() {
        super.onResume()
        loading()
    }

    /**
     * Load messages
     */
    private fun loading() {
        swipeLayout.isRefreshing = true
        GetUsersAsyncTask(this@UsersFragment.activity).execute()
    }

    /**
     * Setup refresh layout
     */
    private fun setupRefreshLayout() {
        swipeLayout.setOnRefreshListener { loading() }
        swipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary)
    }

    /**
     * Setup recycler view.
     */
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        adapter = UserAdapter(this.activity)
        recyclerView.adapter = adapter

        // Add this.
        // Two scroller could have problem.
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(view: RecyclerView?, scrollState: Int) {}

            override fun onScrolled(recyclerView: RecyclerView?, i: Int, i2: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipeLayout.isEnabled = topRowVerticalPosition >= 0
            }
        })
    }

    /**
     * AsyncTask for sign-in
     */
    protected inner class GetUsersAsyncTask(private val context: Context) : AsyncTask<String, Void, HttpResponse>() {

        override fun doInBackground(vararg params: String): HttpResponse? {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null
            }

            return APIRequestHelper.doGet(context.getString(R.string.url_users), null, Session.Companion.instance.token)
        }

        public override fun onPostExecute(rs: HttpResponse?) {
            if (rs != null && rs.body != null) {
                adapter.setUser(JSONParser.getUsers(rs.body))
            }
            swipeLayout.isRefreshing = false
        }
    }
}
