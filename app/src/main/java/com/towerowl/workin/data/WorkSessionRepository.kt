package com.towerowl.workin.data

import android.app.Activity
import android.util.Log
import com.towerowl.workin.App
import com.towerowl.workin.events.WorkSessionEvent
import com.towerowl.workin.utils.TAG
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class WorkSessionRepository
@Inject constructor(@Inject var workSessionDao : WorkSessionDao, val activity : Activity)
{
    private var openSession : WorkSession? = null

    fun insertWorkSession(workSession : WorkSession) {
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.insertWorkSession(workSession)
            }
        }

        //streamPublished.onNext(WorkSessionEvent.Inserted(workSession))
    }

    fun removeWorkSession(workSession : WorkSession) {
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.deleteWorkSession(workSession)
            }
        }

        //streamPublished.onNext(WorkSessionEvent.Removed(workSession))
    }

    fun updateWorkSession(workSession: WorkSession){
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.updateWorkSession(workSession)
            }
        }

        //streamPublished.onNext(WorkSessionEvent.Updated(workSession))
    }

    fun setOpenWorkSession(workSession : WorkSession){
        closeOpenWorkSession()
        openSession = workSession
    }

    fun closeOpenWorkSession(){
        openSession?.let {
            it.closedAt = Calendar.getInstance()
            updateWorkSession(it)
            openSession = null
        }
    }

    fun getWorkSessions() {
        workSessionDao.getWorkSessions()
    }

    fun getWorkSession(id : String) {
        workSessionDao.getWorkSession(id)
    }

    fun isSessionOpen() : Boolean = openSession != null
}