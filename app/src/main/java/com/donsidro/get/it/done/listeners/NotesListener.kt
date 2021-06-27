package com.donsidro.get.it.done.listeners

import com.donsidro.get.it.done.modules.Note

interface NotesListener {
    fun onNoteClicked(note: Note, position: Int)
}