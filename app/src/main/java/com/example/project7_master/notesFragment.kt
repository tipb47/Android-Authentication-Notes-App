package com.example.project7_master

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.project7_master.databinding.FragmentNotesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [notesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class notesFragment : Fragment()   {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : NoteViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        fun noteClicked (note : Note) {
            viewModel.onNoteClicked(note)
        }

        fun yesPressed(noteId : String) {
            Log.d("notesFragment","in yesPressed(): noteId = $noteId")
            //TODO: delete the task with id = taskId
            binding.viewModel?.deleteNote(noteId)
        }

        fun deleteClicked (noteId : String) {
            ConfirmDeletePopUp(noteId,::yesPressed).show(childFragmentManager,
                ConfirmDeletePopUp.TAG)
        }
        val adapter = NoteAdapter(::noteClicked)


        binding.notesList.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToNote.observe(viewLifecycleOwner, Observer { noteId ->
            noteId?.let {
                val action = notesFragmentDirections.actionNotesFragmentToNoteScreen(noteId)
                this.findNavController().navigate(action)
                viewModel.onNoteNavigated()
            }
        })

        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                this.findNavController().navigate(R.id.action_notesFragment_to_signInFragment)
                viewModel.onNavigatedToSignIn()
            }
        })

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}