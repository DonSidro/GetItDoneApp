package com.donsidro.get.it.done.ui.notesview

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel @ViewModelInject constructor(
    private val repository: NoteRepository
): ViewModel() {

    private val searchNoteLiveData = MutableLiveData<String>("")


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

}