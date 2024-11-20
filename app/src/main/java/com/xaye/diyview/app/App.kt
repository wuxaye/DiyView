package com.xaye.diyview.app

import android.app.Application
import com.xaye.helper.BuildConfig
import com.xaye.helper.base.DevHelper

/**
 * Author xaye
 * @date: 2024/11/20
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DevHelper.init(this, BuildConfig.DEBUG)
    }
}