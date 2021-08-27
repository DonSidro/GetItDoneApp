package com.donsidro.get.it.done

import android.app.Application
import com.donsidro.get.it.done.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(noteRepositoryModule)
            modules(firebaseModule)
            modules(firebaseRepositoryModule)
            modules(databaseModule)
            modules(viewModelModule)
        }
    }

}