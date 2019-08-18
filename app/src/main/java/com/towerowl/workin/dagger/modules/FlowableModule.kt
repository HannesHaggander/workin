package com.towerowl.workin.dagger.modules

import com.towerowl.workin.events.WorkSessionEvent
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import javax.inject.Singleton

@Module
class FlowableModule {
   @Provides @Singleton
   fun workSessionStream() : Flowable<WorkSessionEvent> = workSessionPublisher()

   @Provides @Singleton
   fun workSessionPublisher() : FlowableProcessor<WorkSessionEvent> = PublishProcessor.create()
}