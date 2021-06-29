package com.donsidro.get.it.done.database

import android.content.Context
import com.donsidro.get.it.done.modules.Note

class RoomDBHelper {


    internal object HOLDER {
        val INSTANCE = RoomDBHelper()
    }

    companion object {
        val instance: RoomDBHelper by lazy { HOLDER.INSTANCE }
    }

     fun roomInsertNote(applicationContext: Context, note: Note){
        val noteDao = NotesDatabase.getInstance(applicationContext.applicationContext)
         noteDao.insertNote(note)
    }

    fun roomUpdateNote(applicationContext: Context,note: Note){
        val noteDao = NotesDatabase.getInstance(applicationContext.applicationContext)
        noteDao.update(note)
    }

     fun roomDeleteNote(applicationContext: Context, note: Note){
        val noteDao = NotesDatabase.getInstance(applicationContext.applicationContext)
         noteDao.deleteNote(note)
    }

    fun roomGetAllNotes(applicationContext: Context): List<Note>{
        val noteDao = NotesDatabase.getInstance(applicationContext.applicationContext)
        return noteDao.getAllNotes()
    }
    fun roomGetNewNote(applicationContext: Context):Note{
        val noteDao = NotesDatabase.getInstance(applicationContext.applicationContext)
        return noteDao.getNewNote()
    }

}