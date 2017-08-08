package com.castrec.stephane.androidcourseskotlin.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.castrec.stephane.androidcourseskotlin.R


import com.castrec.stephane.androidcourseskotlin.model.User
import com.castrec.stephane.androidcourseskotlin.utils.DateHelper
import com.squareup.picasso.Picasso

import java.text.ParseException

/**
 * Created by sca on 07/06/15.
 */
class UserAdapter(internal var context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    internal var users: List<User>? = null

    fun setUser(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = (context as Activity).layoutInflater
        val convertView = inflater.inflate(R.layout.item_user, parent, false)
        val vh = ViewHolder(convertView)
        return vh
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.username.text = users!![position].username
        /*if (users!![position].urlPhoto != null) {
            Picasso.with(context).load(users!![position].urlPhoto)
                    .resize(100, 100)
                    .centerCrop()
                    .error(R.drawable.ic_account_circle_black_48dp)
                    .into(vh.img)
        } else {
            vh.img.setImageResource(R.drawable.ic_account_circle_black_48dp)
        }*/
        vh.img.setImageResource(R.drawable.ic_account_circle_black_48dp)


        try {
            vh.date.setText(DateHelper.getFormattedDate(users!![position].date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        if (users == null) {
            return 0
        }
        return users!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var username: TextView
        internal var date: TextView
        internal var img: ImageView

        init {
            username = itemView.findViewById(R.id.user_name) as TextView
            date = itemView.findViewById(R.id.user_date) as TextView
            img = itemView.findViewById(R.id.user_img) as ImageView
        }
    }
}
