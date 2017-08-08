package com.castrec.stephane.androidcourseskotlin.session

/**
 * Created by sca on 08/08/17.
 */

internal class Session private constructor() {

    var token: String? = null

    companion object {
        val instance = Session()
    }
}
