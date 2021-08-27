package com.donsidro.get.it.done.ui.notesdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.data.repository.NoteRepository

class NoteDetailViewModel (
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