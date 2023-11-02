package com.example.project7_master

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.project7_master.databinding.FragmentSignUpBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [signUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class signUpFragment : Fragment() {
    val TAG = "signUpFragment"
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : NoteViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signUpFragment_to_signInFragment)
                viewModel.onNavigatedToSignIn()
            }
        })

        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })

        binding.buttonSignUp.setOnClickListener {
            viewModel.signUp(binding.editTextEmail.text.toString(),binding.editTextPassword.text.toString(),binding.editTextReEnterPassword.text.toString())
        }

        return view
    }


}