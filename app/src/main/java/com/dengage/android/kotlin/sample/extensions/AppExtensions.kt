package com.dengage.android.kotlin.sample.extensions

import android.app.Activity
import com.dengage.android.kotlin.sample.App
import com.dengage.sdk.DengageEvent
import com.dengage.sdk.DengageManager

/**
 * Created by Batuhan Coskun on 02 December 2020
 */

fun Activity.getDengageManager(): DengageManager {
    return (this.application as App).dengageManager
}

fun Activity.getDengageEvent(): DengageEvent {
    return (this.application as App).dengageEvent
}