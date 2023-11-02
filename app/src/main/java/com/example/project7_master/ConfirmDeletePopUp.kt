package com.example.project7_master

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class ConfirmDeletePopUp (val noteId : String,val clickListener: (noteId: String) -> Unit) : DialogFragment() {
    val TAG = "ConfirmDeletePopup"
    interface myClickListener {
        fun yesPressed()
    }

    var listener: myClickListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _,_ -> clickListener(noteId)}
            .setNegativeButton("No") { _,_ -> }

            .create()

    companion object {
        const val TAG = "ConfirmDeletePopup"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as myClickListener
        } catch (e: Exception) {
            Log.d("ConfirmDeletePopup", e.message.toString())
        }
    }




}