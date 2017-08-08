package com.castrec.stephane.androidcourseskotlin.utils

import com.castrec.stephane.androidcourseskotlin.model.Note
import com.castrec.stephane.androidcourseskotlin.model.User

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.LinkedList

/**
 * Created by sca on 08/08/17.
 */

object JSONParser {
    @Throws(JSONException::class)
    fun getNotes(json: String): List<Note> {
        val notes = LinkedList<Note>()
        val array = JSONArray(json)
        var obj: JSONObject
        var msg: Note
        for (i in 0..array.length() - 1) {
            obj = array.getJSONObject(i)
            msg = Note(obj.optString("id"), obj.optString("username"), obj.optString("note"),
                    obj.optLong("date"), obj.optBoolean("done", false))
            notes.add(msg)
        }

        return notes
    }

    @Throws(JSONException::class)
    fun getToken(response: String): String {
        return JSONObject(response).optString("token")
    }

    @Throws(JSONException::class)
    fun getUsers(response: String): List<User> {
        val array = JSONArray(response)
        val users = LinkedList<User>()
        var obj: JSONObject
        var u: User
        for (i in 0..array.length() - 1) {
            obj = array.getJSONObject(i)
            u = User(obj.optString("username"), obj.optLong("date"),
                    obj.optString("urlPhoto"))
            users.add(u)
        }
        return users
    }
}
