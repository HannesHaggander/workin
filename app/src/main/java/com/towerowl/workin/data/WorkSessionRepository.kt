package com.towerowl.workin.data

import com.towerowl.workin.events.WorkSessionEvent
import io.reactivex.processors.FlowableProcessor
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Inject

class WorkSessionRepository
@Inject constructor(private val workSessionDao : WorkSessionDao, val publishStream : FlowableProcessor<WorkSessionEvent>)
{
    private var openSession : WorkSession? = null

    init {
        GlobalScope.launch {
            openSession = workSessionDao.getWorkSessions()
                .firstOrNull { it.closedAt == null }
        }
    }

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

    fun getWorkSessionsFromToday() : List<WorkSession> =
        workSessionDao.getWorkSessions()
            .filter { it.isDay(LocalDate.now()) }

    fun setOpenWorkSession(workSession : WorkSession){
        closeOpenWorkSession()
        openSession = workSession
    }

    fun closeOpenWorkSession(){
        openSession?.let {
            it.closedAt = OffsetDateTime.now()
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