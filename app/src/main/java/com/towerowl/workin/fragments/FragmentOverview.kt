package com.towerowl.workin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.towerowl.workin.App
import com.towerowl.workin.R
import com.towerowl.workin.activities.ActivityMain
import com.towerowl.workin.adapters.SessionAdapter
import com.towerowl.workin.data.WorkSession
import com.towerowl.workin.events.WorkSessionEvent
import com.towerowl.workin.viewmodels.OverviewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class FragmentOverview : Fragment() {

    private lateinit var toggleButton: AppCompatButton
    private lateinit var sessionList: RecyclerView
    private lateinit var timeSumText : AppCompatTextView

    private val mAdapter: SessionAdapter = SessionAdapter()

    private lateinit var overviewModel : OverviewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_overview, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupViews()
        setupButtons()
        setupRecycler()
        setupStreams()
    }


    private fun setupViewModel(){
        overviewModel = ViewModelProvider(this).get(OverviewModel::class.java)
            .apply { activity = requireActivity() }
            .also {
                it.workSessionsToday.observe(this, Observer {
                        data -> workSessionTodayObserver(data)
                })

                it.getWorkSessionsFromToday()
            }
    }


    private fun workSessionTodayObserver(sessions : List<WorkSession>){
        sessions.forEach { x -> mAdapter.add(x) }
        sessions.sumBy { session ->
            ChronoUnit.SECONDS.between(session.createdAt,
                session.closedAt ?: OffsetDateTime.now()).toInt()
            }
            .also { seconds ->
                val minutes = seconds / 60
                val hours = minutes / 24

                timeSumText.text =
                    "$hours ${requireContext().getString(R.string.hours).toLowerCase()}, " +
                    "${minutes%60} ${requireContext().getString(R.string.minutes).toLowerCase()}"
            }
    }


    private fun setupViews() {
        toggleButton = requireView().findViewById(R.id.f_overview_toggle_session)
        sessionList = requireView().findViewById(R.id.f_overview_activity_recycler)
        timeSumText = requireView().findViewById(R.id.f_overview_time_display)
        requireView().findViewById<AppCompatImageButton>(R.id.fragment_overview_settings)
            .also {
                it.setOnClickListener {
                    (activity as ActivityMain).swapFragment(FragmentSettings())
                }
            }
    }


    private fun setupButtons() {
        App.get(requireActivity())
            .appComponent
            .workSessionRepository()
            .also { repo ->
                toggleButton.setOnClickListener {
                    if(repo.isSessionOpen()){
                        repo.closeOpenWorkSession()
                        toggleButton.setText(R.string.start)
                    }
                    else {
                        val session = WorkSession()
                        repo.insertWorkSession(session)
                            .also { repo.setOpenWorkSession(session) }
                        toggleButton.setText(R.string.stop)
                    }
                }
            }
    }

    private fun setupStreams() =
        App.get(requireActivity())
            .appComponent
            .workSessionRepository()
            .publishStream
            .doOnNext {
                when(it){
                    is WorkSessionEvent.Inserted -> { mAdapter.add(it.data) }

                    is WorkSessionEvent.Removed  -> { mAdapter.remove(it.data) }

                    is WorkSessionEvent.Updated  -> {
                        overviewModel.getWorkSessionsFromToday()
                        mAdapter.update(it.data)
                    }

                    else -> throw Exception("Unhandled work session event")
                }
            }
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()


    private fun setupRecycler() {
        sessionList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, true)
        sessionList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        sessionList.adapter = mAdapter
    }
}