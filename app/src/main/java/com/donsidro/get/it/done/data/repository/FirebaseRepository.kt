package com.donsidro.get.it.done.data.repository

import com.donsidro.get.it.done.utils.FirebaseSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val fireBaseSource: FirebaseSource) {

    fun signInWithGoogle(acct: GoogleSignInAccount) = fireBaseSource.signInWithGoogle(acct)

    fun fetchUser() = fireBaseSource.fetchUser()
}