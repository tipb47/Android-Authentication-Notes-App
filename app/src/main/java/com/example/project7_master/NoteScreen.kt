package com.example.project7_master

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.project7_master.databinding.FragmentNoteScreenBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoteScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteScreen : Fragment() {
    private var _binding: FragmentNoteScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        val noteId = NoteScreenArgs.fromBundle(requireArguments()).noteId

        val viewModel : NoteViewModel by activityViewModels()
        viewModel.noteId = noteId
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_noteScreen_to_notesFragment)
                viewModel.onNavigatedToList()
            }
        })

        //saveNote() call on click wasn't working in xml, so set a clicklistener here.
        binding.saveButton.setOnClickListener {
            viewModel.updateNote()
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

        binding.deleteNoteButton.setOnClickListener {
            deleteClicked(noteId)
        }

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}