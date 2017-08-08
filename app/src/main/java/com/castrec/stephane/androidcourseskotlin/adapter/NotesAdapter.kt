package com.castrec.stephane.androidcourseskotlin.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.castrec.stephane.androidcourseskotlin.R
import com.castrec.stephane.androidcourseskotlin.model.Note
import com.castrec.stephane.androidcourseskotlin.utils.DateHelper
import java.text.ParseException
import java.util.*

/**
 * Created by sca on 02/06/15.
 */
class NotesAdapter(private val context: Context, private val listener: CompoundButton.OnCheckedChangeListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    internal var notes: List<Note>? = LinkedList()

    fun addNotes(notes: List<Note>) {
        this.notes = notes
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val inflater = (context as Activity).layoutInflater
        val convertView = inflater.inflate(R.layout.item_note, parent, false)
        val vh = ViewHolder(convertView)
        return vh
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.username.text = notes!![position].username
        vh.message.setText(notes!![position].message)
        vh.done.isChecked = notes!![position].isDone
        vh.done.setOnCheckedChangeListener(listener)
        vh.done.tag = position
        try {
            vh.date.setText(DateHelper.getFormattedDate(notes!![position].date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        if (notes == null) {
            return 0
        }
        return notes!!.size
    }

    fun getItem(position: Int?): Note {
        return notes!![position!!]
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var username: TextView
        internal var message: TextView
        internal var date: TextView
        internal var done: CheckBox

        init {
            username = itemView.findViewById(R.id.note_user) as TextView
            message = itemView.findViewById(R.id.note_message) as TextView
            date = itemView.findViewById(R.id.note_date) as TextView
            done = itemView.findViewById(R.id.not_check) as CheckBox
        }
    }
}
