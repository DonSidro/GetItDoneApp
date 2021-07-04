package com.donsidro.get.it.done.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "note_table")
data class Note(

    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,

    var title: String? = null,
    var subTitle: String? = null,
    var body: String? = null,
    var dateTime: String? = null,
    var imagePath: String? = "",
    var color: String? = null,
    var webLink: String? = "",
    var position: Int? = id





)
