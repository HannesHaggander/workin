package com.towerowl.workin.data

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class WorkSessionRepository
    private constructor(private val workSessionDao: WorkSessionDao)
{
    companion object{
        @Volatile private var instance : WorkSessionRepository? = null

        fun getInstance(workSessionDao: WorkSessionDao) : WorkSessionRepository{
            return instance ?: synchronized(this){
                instance ?: WorkSessionRepository(workSessionDao).also { instance = it }
            }
        }
    }

    private val streamPublished : PublishProcessor<WorkSessionEvent> = PublishProcessor.create()
    val messageFlow = streamPublished as Flowable<WorkSessionEvent>
    private var openSession : WorkSession? = null

    fun insertWorkSession(workSession : WorkSession) {
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.insertWorkSession(workSession)
            }
        }

        streamPublished.onNext(WorkSessionEvent.Inserted(workSession))
    }

    fun removeWorkSession(workSession : WorkSession) {
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.deleteWorkSession(workSession)
            }
        }

        streamPublished.onNext(WorkSessionEvent.Removed(workSession))
    }

    fun updateWorkSession(workSession: WorkSession){
        GlobalScope.launch {
            withContext(IO){
                workSessionDao.updateWorkSession(workSession)
            }
        }

        streamPublished.onNext(WorkSessionEvent.Updated(workSession))
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
}