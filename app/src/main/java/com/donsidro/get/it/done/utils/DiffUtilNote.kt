package com.donsidro.get.it.done.utils

import androidx.recyclerview.widget.DiffUtil
import com.donsidro.get.it.done.data.entities.Note

class DiffUtilNote (

    private val oldList: List<Note>,
    private val newList: List<Note>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
                    && oldList[oldItemPosition].title == newList[newItemPosition].title
                    && oldList[oldItemPosition].subTitle == newList[newItemPosition].subTitle
                    && oldList[oldItemPosition].imagePath == newList[newItemPosition].imagePath
                    && oldList[oldItemPosition].webLink == newList[newItemPosition].webLink
                    && oldList[oldItemPosition].body == newList[newItemPosition].body
                    && oldList[oldItemPosition].dateTime == newList[newItemPosition].dateTime


        }
}