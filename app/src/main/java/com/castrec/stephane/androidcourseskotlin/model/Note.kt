package com.castrec.stephane.androidcourseskotlin.model

/**
 * Created by sca on 08/08/17.
 */

data class Note(val id: String, val username: String, val message: String, val date: Long, val isDone: Boolean)