package com.example.technicaltest

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.technicaltest.util.LifecycleAwareUserManager
import org.junit.Assert.assertNull
import org.junit.Test

class UserManagerTest {

    @Test
    fun lifecycleAwareUserManager_onDestroy_clearsReference() {
        val manager = LifecycleAwareUserManager()
        
        val fakeOwner = object : LifecycleOwner {
            override val lifecycle: Lifecycle
                get() = throw UnsupportedOperationException()
        }
        
        manager.onDestroy(fakeOwner)

        assertNull("Activity reference harus null setelah onDestroy", manager.getActivity())
    }
}
