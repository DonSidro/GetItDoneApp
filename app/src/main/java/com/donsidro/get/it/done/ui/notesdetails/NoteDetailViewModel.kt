package com.donsidro.get.it.done.ui.notesdetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.data.entities.User
import com.donsidro.get.it.done.data.repository.FirebaseRepository
import com.donsidro.get.it.done.data.repository.NoteRepository
import com.donsidro.get.it.done.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth

class NoteDetailViewModel @ViewModelInject constructor(
    private val repository: NoteRepository

): ViewModel() {

    private val _id = MutableLiveData<Int>()

    private val _note = _id.switchMap { id ->
        repository.getNote(id)
    }
    val note: LiveData<Note> = _note


    fun start(id: Int) {
        _id.value = id
    }

    fun saveNote(note: Note) {
        repository.insertNote(note)
    }

}