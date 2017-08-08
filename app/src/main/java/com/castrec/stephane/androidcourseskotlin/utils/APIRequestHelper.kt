package com.castrec.stephane.androidcourseskotlin.utils

import android.util.Log
import com.castrec.stephane.androidcourseskotlin.model.HttpResponse
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Created by sca on 08/08/17.
 */

object APIRequestHelper {
    fun doGet(purl: String?, params: Map<String, String>?, token: String?): HttpResponse? {

        // Un stream pour récevoir la réponse
        var inputStream: InputStream? = null
        if (purl == null) {
            Log.e("CESI", "Error url to call empty")
            throw RuntimeException("Error url to call empty")
        }

        try {
            val sb = StringBuilder(purl)
            sb.append("?")
            sb.append(concatParams(params))

            val url = URL(sb.toString())
            Log.d("Calling URL", url.toString())
            val conn = url.openConnection() as HttpURLConnection

            conn.readTimeout = 10000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            conn.doInput = true

            if (token != null) {
                //set authorization header
                conn.setRequestProperty("token", token)
            }

            // Starts the query
            conn.connect()
            val response = conn.responseCode
            Log.d("APIRequestHelper", "The response code is: " + response)

            if (response != 200) {
                return HttpResponse(status = response, body = null!!)
            } else {
                inputStream = conn.inputStream

                // Convert the InputStream into a string
                var body = inputStream.bufferedReader().use { it.readText() }
                return HttpResponse(status = response, body = body)

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }  finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun doPost(purl: String?, params: Map<String, String>, token: String?): HttpResponse? {

        // Un stream pour récevoir la réponse
        var inputStream: InputStream? = null
        if (purl == null) {
            Log.e("CESI", "Error url to call empty")
            throw RuntimeException("Error url to call empty")
        }

        try {
            val url = URL(purl)
            Log.d("Calling URL", url.toString())
            val conn = url.openConnection() as HttpURLConnection

            conn.readTimeout = 10000
            conn.connectTimeout = 15000
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true

            if (token != null) {
                //set authorization header
                conn.setRequestProperty("token", token)
            }

            val os = conn.outputStream
            val writer = BufferedWriter(
                    OutputStreamWriter(os, "UTF-8"))
            writer.write(concatParams(params))
            writer.flush()
            writer.close()
            os.close()
            // Starts the query
            conn.connect()
            val response = conn.responseCode
            Log.d("APIRequestHelper", "The response code is: " + response)

            if (response != 200) {
                return HttpResponse(status = response, body = null!!)
            } else {
                inputStream = conn.inputStream

                // Convert the InputStream into a string
                var body = inputStream.bufferedReader().use { it.readText() }
                return HttpResponse(status = response, body = body)

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }
        }  finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }

    /**
     * Concat params to be send
     * @param params
     * *
     * @return
     */
    private fun concatParams(params: Map<String, String>?): String {
        val sb = StringBuilder()
        if (params != null && params.size > 0) {
            for ((key, value) in params) {
                try {
                    sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&")
                } catch (e: UnsupportedEncodingException) {
                    Log.e("Cesi", "Error adding param", e)
                }

            }
        }
        return sb.toString()
    }
}
