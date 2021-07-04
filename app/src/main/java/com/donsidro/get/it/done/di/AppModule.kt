package com.donsidro.get.it.done.di

import android.content.Context
import com.donsidro.get.it.done.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = NoteDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideNoteDao(db: NoteDatabase) = db.noteDao()

}