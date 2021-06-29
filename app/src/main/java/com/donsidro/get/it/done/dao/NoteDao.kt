package com.donsidro.get.it.done.dao

import androidx.room.*
import com.donsidro.get.it.done.modules.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY position DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes ORDER BY id DESC LIMIT 1")
    fun getNewNote(): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun update(note: Note)

}