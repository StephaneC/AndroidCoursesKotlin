package com.castrec.stephane.androidcourseskotlin.fragment


import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.castrec.stephane.androidcourseskotlin.R
import com.castrec.stephane.androidcourseskotlin.model.HttpResponse
import com.castrec.stephane.androidcourseskotlin.session.Session
import com.castrec.stephane.androidcourseskotlin.utils.APIRequestHelper

/**
 * Created by sca on 04/06/15.
 */
class WriteNotesDialog : DialogFragment() {

    private var note: EditText? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity.layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val view = inflater.inflate(R.layout.dialog_note, null)
        note = view.findViewById(R.id.note_msg) as EditText?

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.validate, DialogInterface.OnClickListener { dialog, id ->
                    closeKeyboard()
                    if (!note!!.text.toString().isEmpty()) {
                        //post note
                        SendNoteAsyncTask(view.getContext()).execute(note!!.text.toString())
                    } else {
                        note!!.error = this@WriteNotesDialog.activity
                                .getString(R.string.error_missing_msg)
                    }
                }).setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
            closeKeyboard()
            this@WriteNotesDialog.dismiss()
        }

        )
        return builder.create()
    }

    private fun closeKeyboard() {
        val imm = this.activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(note!!.windowToken, 0)
    }

    /**
     * AsyncTask for sign-in
     */
    protected inner class SendNoteAsyncTask(internal var context: Context) : AsyncTask<String, Void, HttpResponse>() {

        override fun doInBackground(vararg params: String): HttpResponse? {
            val map = mapOf("note" to params[0])

            return APIRequestHelper.doPost(context.getString(R.string.url_notes), map, Session.instance.token)
        }

        public override fun onPostExecute(rs: HttpResponse?) {
            if (rs != null && rs.status !== 200) {
                Toast.makeText(context, context.getString(R.string.error_send_msg), Toast.LENGTH_SHORT).show()
            } else {
                this@WriteNotesDialog.dismiss()
            }
        }
    }

    companion object {
        fun getInstance(token: String?): WriteNotesDialog {
            val f = WriteNotesDialog()

            // Supply num input as an argument.
            val args = Bundle()
            args.putString("token", token)
            f.arguments = args

            return f
        }
    }
}
