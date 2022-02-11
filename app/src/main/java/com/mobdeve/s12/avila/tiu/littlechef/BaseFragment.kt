package com.mobdeve.s12.avila.tiu.littlechef

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(),CoroutineScope{

    /*
        References - Read:https://stackoverflow.com/questions/53324408/why-must-dispatchers-main-be-added-to-the-root-job-of-an-implementation-of-an
        "The advantage of providing Dispatchers.Main in coroutineContext is that
        you don't have to supply it every time, so you can just write
        launch { ... }"
        "The + syntax works because each object you add declares its own map key,
        which + uses to put it into the context's internal map."
     */
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job +Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}