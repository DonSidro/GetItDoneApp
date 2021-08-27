package com.donsidro.get.it.done.ui.notesview

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.data.entities.User
import com.donsidro.get.it.done.data.repository.FirebaseRepository
import com.donsidro.get.it.done.data.repository.NoteRepository
import com.donsidro.get.it.done.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth

class NotesViewModel(
    private val repository: NoteRepository,
    private val repositoryFirebase: FirebaseRepository,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val searchNoteLiveData = MutableLiveData<String>("")
    private val gMailUserLiveData = MutableLiveData<Resource<User>>()


    var notes: LiveData<List<Note>> = Transformations.switchMap(searchNoteLiveData)
    {
        string->
        when {
            TextUtils.isEmpty(string.toString()) -> {
                repository.getAllNotes()
            }
            string.equals("_chip_image_") -> {
                repository.getNotesFromSearchChip(string.toString())
            }
            string.equals("_chip_url_") -> {
                repository.getNotesFromSearchChip(string.toString())
            }
            string.equals("_chip_image_url_") -> {
                repository.getNotesFromSearchChip(string.toString())
            }
            else -> {
                repository.getNotesFromSearch(string.toString())

            }
        }
    }

    fun searchNoteChanged(name: String) {
        searchNoteLiveData.value = name
    }
    fun searchNoteChangedFilter(name: List<String>) {
        when {
            name.isEmpty() -> searchNoteLiveData.value = ""
            name.size == 1 -> searchNoteLiveData.value = name[0]
            name.size == 2 -> searchNoteLiveData.value = "_chip_image_url_"
        }
    }

    fun signInWithGoogle(acct: GoogleSignInAccount): LiveData<Resource<User>> {
        repositoryFirebase.signInWithGoogle(acct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                gMailUserLiveData.postValue(
                    Resource.success(
                        User(
                            firebaseAuth.currentUser?.email!!,
                            firebaseAuth.currentUser?.displayName!!,
                            firebaseAuth.currentUser?.photoUrl!!
                        )
                    )
                )
            } else {
                gMailUserLiveData.postValue(Resource.error(null, "couldn't sign in user"))
            }

        }
        return gMailUserLiveData
    }

    fun saveNote(note: Note) {
        repository.insertNote(note)
    }

}