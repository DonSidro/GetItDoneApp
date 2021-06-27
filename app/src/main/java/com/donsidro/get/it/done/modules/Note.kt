package com.donsidro.get.it.done.modules

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "notes")
data class Note(

    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,

    var title: String? = null,
    var subTitle: String? = null,
    var body: String? = null,
    var dateTime: String? = null,
    var imagePath: String? = null,
    var color: String? = null,
    var webLink: String? = null





) : Serializable
