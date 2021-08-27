package com.donsidro.get.it.done.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.donsidro.get.it.done.R

sealed class MenuAction(
    @StringRes val label: Int,
    @DrawableRes val icon: Int) {

    object Share : MenuAction(R.string.add, R.drawable.ic_add)
}