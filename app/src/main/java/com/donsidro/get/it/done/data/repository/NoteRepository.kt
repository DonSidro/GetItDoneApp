package com.donsidro.get.it.done.data.repository

import androidx.lifecycle.LiveData
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.data.local.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {

    fun getAllNotes() : LiveData<List<Note>>{
        return noteDao.getAllNotes()
    }
    fun getNotesFromSearch(filter: String) : LiveData<List<Note>>{
        return noteDao.getNotesFromSearch(filter)
    }
    fun getNotesFromSearchChip(filter: String) : LiveData<List<Note>>{
        return when (filter) {
            "_chip_image_" -> {
                noteDao.getNotesFromSearchChipImage()
            }
            "_chip_image_url_" -> {
                noteDao.getNotesFromSearchChipBoth()
            }
            "_chip_url_" -> {
                noteDao.getNotesFromSearchChipURL()
            }
            else -> noteDao.getNotesFromSearch(filter)
        }
    }

    fun getNewestNote() : Note{
        return noteDao.getNewestNote()
    }

    fun getNote(noteid : Int) : LiveData<Note>{
        return noteDao.getNote(noteid)
    }

    fun insertNote(note: Note){
        CoroutineScope(IO).launch {
            noteDao.insertNote(note)
        }
    }
    fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }
    fun updateNote(note: Note){
        noteDao.updateNote(note)
    }

}