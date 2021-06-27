package com.donsidro.get.it.done.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.donsidro.get.it.done.dao.NoteDao
import com.donsidro.get.it.done.modules.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        var TEST_MODE = false

        private const val databaseName = "notesDB"

        private var db: NotesDatabase? = null
        private var dbInstance: NoteDao? = null

        fun getInstance(context: Context): NoteDao {
            if (dbInstance == null) {
                if (TEST_MODE) {
                    db = Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java).allowMainThreadQueries().build()
                    dbInstance = db?.getNoteDao()
                } else {
                    db = Room.databaseBuilder(context, NotesDatabase::class.java, databaseName).build()
                    dbInstance = db?.getNoteDao()
                }
            }
            return dbInstance!!
        }

        private fun close() {
            db?.close()
        }
    }
}