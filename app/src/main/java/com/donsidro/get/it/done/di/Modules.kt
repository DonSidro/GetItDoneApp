package com.donsidro.get.it.done.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.donsidro.get.it.done.data.local.NoteDao
import com.donsidro.get.it.done.data.local.NoteDatabase
import com.donsidro.get.it.done.data.repository.FirebaseRepository
import com.donsidro.get.it.done.data.repository.NoteRepository
import com.donsidro.get.it.done.ui.notesview.NotesViewModel
import com.donsidro.get.it.done.utils.FirebaseSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val noteRepositoryModule = module {

    fun provideRepository(dao : NoteDao): NoteRepository {
        return NoteRepository(dao)
    }
    single { provideRepository(get()) }

}

val firebaseRepositoryModule = module {

    fun provideRepository(firebaseSource: FirebaseSource): FirebaseRepository {
        return FirebaseRepository(firebaseSource)
    }
    single { provideRepository(get()) }

}


val viewModelModule = module {
    viewModel {
        NotesViewModel(repository = get(),firebaseAuth = get(), repositoryFirebase = get() )
    }

}

val firebaseModule = module {

    fun provideGso(context: Context) : GoogleSignInOptions{
        return  GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("133466663932-sjjcno6tuva8ar970lkcl836na2e840u.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    fun provideGoogleClient(context: Context, gso: GoogleSignInOptions): GoogleSignInClient{
        return GoogleSignIn.getClient(context, gso)
    }

    fun provideFirebaseSource(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore) : FirebaseSource{
        return FirebaseSource(firebaseAuth, firestore)
    }


    single { FirebaseAuth.getInstance() }

    single { FirebaseFirestore.getInstance() }

    single { provideGso(androidContext()) }

    single { provideGoogleClient(androidContext(), get()) }

    single { provideFirebaseSource(get(), get()) }

}


val databaseModule = module {

    fun provideDatabase(application: Application): NoteDatabase {
        return Room.databaseBuilder(application, NoteDatabase::class.java, "notes")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCountriesDao(database: NoteDatabase): NoteDao {
        return  database.notesDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }
}