package com.donsidro.get.it.done.modules

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "notes")
data class Note(

    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,

    var title: String? = null,
    var subTitle: String? = null,
    var body: String? = null,
    var dateTime: String? = null,
    val imagePath: String? = null,
    var color: String? = null,
    val webLink: String? = null





)
