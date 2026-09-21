package com.example.technicaltest.util

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 * Soal 4: Kode asli yang bermasalah:
 *
 * object UserManager {
 *     var activity: Activity? = null
 *     fun setActivity(activity: Activity) {
 *         this.activity = activity
 *     }
 * }
 *
 * Masalah: Hard reference ke Activity di dalam Singleton (Static) menahan GC me-reclaim memory Activity.
 * Risiko: Memory Leak, OOM Crash saat rotasi layar, dan Stale State Crash.
 */

// Solusi 1 (Rekomendasi Utama): Menggunakan Application Context
class SafeUserManager private constructor(private val appContext: Context) {
    companion object {
        @Volatile
        private var INSTANCE: SafeUserManager? = null

        fun getInstance(context: Context): SafeUserManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SafeUserManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun getApplicationContext(): Context = appContext
}

// Solusi 2: Menggunakan WeakReference + DefaultLifecycleObserver
class LifecycleAwareUserManager : DefaultLifecycleObserver {
    private var activityRef: WeakReference<Activity>? = null

    fun attachActivity(activity: ComponentActivity) {
        activityRef = WeakReference(activity)
        activity.lifecycle.addObserver(this)
    }

    fun getActivity(): Activity? = activityRef?.get()

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        activityRef?.clear()
        activityRef = null
    }
}
