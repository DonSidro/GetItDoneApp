package com.donsidro.get.it.done.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.donsidro.get.it.done.data.entities.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table where title like :filter ||'%' or LOWER(title) like LOWER(:filter) ||'%' or subTitle LIKE :filter ||'%'")
    fun getNotesFromSearch(filter: String): LiveData<List<Note>>

    @Query("SELECT * FROM note_table where imagePath != ''")
    fun getNotesFromSearchChipImage(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table where imagePath != '' or webLink != ''")
    fun getNotesFromSearchChipBoth(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table where webLink != ''")
    fun getNotesFromSearchChipURL(): LiveData<List<Note>>


    @Query("SELECT * FROM note_table ORDER BY id DESC LIMIT 1")
    fun getNewestNote(): Note

    @Query("SELECT * FROM note_table WHERE id=:noteId ")
    fun getNote(noteId : Int): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)

}