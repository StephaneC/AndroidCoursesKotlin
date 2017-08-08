package com.castrec.stephane.androidcourseskotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

/**
 * Created by sca on 08/08/17.
 */

internal object NetworkHelper {

    fun isInternetAvailable(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (e: Exception) {
            Log.e("HelloWorld", "Error on checking internet:", e)

        }

        //default allowed to access internet
        return true

    }
}
