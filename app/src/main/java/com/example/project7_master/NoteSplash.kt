package com.example.project7_master

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoteSplash.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteSplash : Fragment() {

    val viewModel : NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_splash, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = viewModel.getCurrentUser()

        val handler = Handler(Looper.myLooper()!!)

        handler.postDelayed({
            //valid user found, load their notes
            if (currentUser != null) {
                this.findNavController().navigate(R.id.action_noteSplash_to_notesFragment)
            }
            //nobody logged in, go sign in
            else {
                this.findNavController().navigate(R.id.action_noteSplash_to_signInFragment)

            }

        }, 2000)
    }


}