package com.donsidro.get.it.done.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.donsidro.get.it.done.data.entities.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val notesDao: NoteDao
}