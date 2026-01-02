package pt.dourobats.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import pt.dourobats.app.core.data.preferences.initDataStore
import pt.dourobats.app.di.initKoin

class DouroBatsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize DataStore
        initDataStore(this)

        // Initialize Koin with Android context
        initKoin {
            androidContext(this@DouroBatsApplication)
        }
    }
}
