package com.kostic_marko.android_app

import android.app.Application
import com.kostic_marko.android_app.di.dispatchersModule
import com.kostic_marko.android_app.di.viewModelModule
import com.kostic_marko.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(dataModule, viewModelModule, dispatchersModule)
        }
    }
}