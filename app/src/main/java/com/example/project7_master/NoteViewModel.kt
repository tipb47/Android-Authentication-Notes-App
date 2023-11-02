package com.example.project7_master

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class NoteViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()

    var user: User = User("","")
    var noteId: String = ""
    var note = MutableLiveData<Note>()
    private val _notes: MutableLiveData<MutableList<Note>> = MutableLiveData()
    val notes: LiveData<List<Note>>
        get() = _notes as LiveData<List<Note>>
    private val _navigateToNote = MutableLiveData<String?>()
    val navigateToNote: LiveData<String?>
        get() = _navigateToNote

    private val _errorHappened = MutableLiveData<String?>()
    val errorHappened: LiveData<String?>
        get() = _errorHappened

    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    private lateinit var notesCollection: DatabaseReference


    init {

        if (noteId.trim() == "") {
            note.value = Note()
        }
        _notes.value = mutableListOf<Note>()
    }

    fun initializeTheDatabase() {

        val database = Firebase.database
        notesCollection = database
            .getReference("notes")
            .child(auth.currentUser!!.uid)


        notesCollection.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //keep track of changing notes database
                var notesList: ArrayList<Note> = ArrayList()
                for (noteSnapshot in dataSnapshot.children) {
                    Log.d("NoteViewModel", "Note Data: $note")
                    var note = noteSnapshot.getValue<Note>()
                    note?.noteId = noteSnapshot.key!!
                    notesList.add(note!!)
                }
                _notes.value = notesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        })

    }

    fun getAll(): LiveData<List<Note>> {
        return notes
    }

    fun updateNote() {
        //new note
        if (noteId.trim() == "") {
            notesCollection.push().setValue(note.value)
        } else { //existing note
            notesCollection.child(noteId).setValue(note.value)
        }
        _navigateToList.value = true
    }

    fun deleteNote(noteId : String) {
        notesCollection.child(noteId).removeValue()
        _navigateToList.value = true
    }

    fun onNoteClicked(selectedNote: Note) {
        _navigateToNote.value = selectedNote.noteId
        noteId = selectedNote.noteId
        note.value = selectedNote
    }

    fun onNewNoteClicked() {
        _navigateToNote.value = ""
        noteId = ""
        note.value = Note()
    }

    fun onNoteNavigated() {
        _navigateToNote.value = null
    }

    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }

    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }

    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }

    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }

    //sign in w/ email, password, throw error if incorrect login
    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                initializeTheDatabase()
                _navigateToList.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    //signup with email, password, ensure password match.
    fun signUp(email: String, password: String, verifyPassword: String) {
        //one of the fields is empty
        if (email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }

        //passwords do not match
        if (!password.equals(verifyPassword)) {
            Log.d("tag", "$password $verifyPassword")
            _errorHappened.value = "Password does not matched re-entered password"
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                user = User(email,password)
                _navigateToSignIn.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _navigateToSignIn.value = true
    }


    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
