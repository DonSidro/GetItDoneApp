package com.donsidro.get.it.done.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseSource(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore) {

    fun signInWithGoogle(acct: GoogleSignInAccount) = firebaseAuth.signInWithCredential(
        GoogleAuthProvider.getCredential(acct.idToken,null))

    fun fetchUser() = firestore.collection("users").get()

}