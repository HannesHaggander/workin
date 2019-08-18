package com.towerowl.workin.data

import com.towerowl.workin.events.WorkSessionEvent
import io.reactivex.processors.FlowableProcessor
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class WorkSessionRepository
@Inject constructor(val workSessionDao : WorkSessionDao, val publishStream : FlowableProcessor<WorkSessionEvent>)
{
    private var openSession : WorkSession? = null

    fun insertWorkSession(workSession : WorkSession) {
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.insertWorkSession(workSession)
            }
        }

        publishStream.onNext(WorkSessionEvent.Inserted(workSession))
    }

    fun removeWorkSession(workSession : WorkSession) {
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.deleteWorkSession(workSession)
            }
        }

        publishStream.onNext(WorkSessionEvent.Removed(workSession))
    }

    fun updateWorkSession(workSession: WorkSession){
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.updateWorkSession(workSession)
            }
        }

        publishStream.onNext(WorkSessionEvent.Updated(workSession))
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