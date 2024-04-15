package tech.pauly.fooder

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FooderApplication : Application() {

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}