package com.dengage.android.kotlin.sample

import android.app.Application
import com.dengage.android.kotlin.sample.utils.Constants
import com.dengage.sdk.DengageEvent
import com.dengage.sdk.DengageLifecycleTracker
import com.dengage.sdk.DengageManager

/**
 * Created by Batuhan Coskun on 02 December 2020
 */
class App : Application() {

    lateinit var dengageManager: DengageManager
    lateinit var dengageEvent: DengageEvent

    override fun onCreate() {
        super.onCreate()

        // to handle application bring to foreground
        registerActivityLifecycleCallbacks(DengageLifecycleTracker())

        // should be initiated once in application runtime
        dengageManager = DengageManager
            .getInstance(applicationContext)
            .setLogStatus(true)
            .setFirebaseIntegrationKey(Constants.FIREBASE_APP_INTEGRATION_KEY)
            .init()

        // should be initiated once in application runtime
        dengageEvent = DengageEvent.getInstance(applicationContext)
    }

}