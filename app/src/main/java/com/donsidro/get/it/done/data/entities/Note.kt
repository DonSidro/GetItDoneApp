package com.donsidro.get.it.done.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "note_table")
data class Note(
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var subTitle: String,
    var body: String,
    var dateTime: String,
    var imagePath: String,
    var color: String,
    var webLink: String,
    var randomFirebaseID: String

)
