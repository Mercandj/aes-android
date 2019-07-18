package com.mercandalli.android.apps.feature_aes_sample

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

object ActivityUtils {

    fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }
}
