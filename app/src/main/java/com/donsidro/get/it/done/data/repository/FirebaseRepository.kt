package com.donsidro.get.it.done.data.repository

import com.donsidro.get.it.done.utils.FirebaseSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class FirebaseRepository(
    private val fireBaseSource: FirebaseSource) {

    fun signInWithGoogle(acct: GoogleSignInAccount) = fireBaseSource.signInWithGoogle(acct)

    fun fetchUser() = fireBaseSource.fetchUser()
}