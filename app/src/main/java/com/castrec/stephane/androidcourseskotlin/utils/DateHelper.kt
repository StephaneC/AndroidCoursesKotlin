package com.castrec.stephane.androidcourseskotlin.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by sca on 03/06/15.
 */
object DateHelper {

    private val format = "HH:mm:ss"
    private var formatter: SimpleDateFormat? = null

    /**
     * create formatted date from timestamp.
     * @param timestamp
     * *
     * @return
     */
    @Throws(ParseException::class)
    fun getFormattedDate(timestamp: Long): String {
        if (formatter == null) {
            formatter = SimpleDateFormat(format)
        }
        return formatter!!.format(Date(timestamp)).toString()
    }
}
