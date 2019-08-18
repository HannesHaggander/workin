package com.towerowl.workin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.towerowl.workin.App
import com.towerowl.workin.R
import com.towerowl.workin.adapters.SessionAdapter
import com.towerowl.workin.data.WorkSession
import com.towerowl.workin.events.WorkSessionEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class FragmentOverview : Fragment() {

    private lateinit var toggleButton: AppCompatButton
    private lateinit var sessionList: RecyclerView
    private lateinit var timeSumText : AppCompatTextView

    private val mAdapter: SessionAdapter = SessionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_overview, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupButtons()
        setupRecycler()
        setupStreams()
        setupWorkSessionsFromToday()
    }


    private fun setupViews() {
        toggleButton = view?.findViewById<AppCompatButton>(R.id.f_overview_toggle_session) ?: throw Exception()
        sessionList = view?.findViewById<RecyclerView>(R.id.f_overview_activity_recycler) ?: throw Exception()
        timeSumText = view?.findViewById<AppCompatTextView>(R.id.f_overview_time_display) ?: throw Exception()
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


    private fun setupWorkSessionsFromToday(){
        GlobalScope.launch {
            val items = App.get(requireActivity())
                .appComponent
                .workSessionRepository()
                .getWorkSessionsFromToday()

            items.forEach { mAdapter.add(it) }
            items.sumBy {
                ChronoUnit.SECONDS.between(it.createdAt, it.closedAt ?: LocalDateTime.now()).toInt()
            }
            .also { seconds ->
                val minutes = seconds / 60
                val hours = minutes / 24
                timeSumText.setText("$hours ${requireContext().getString(R.string.hours).toLowerCase()}, " +
                        "${minutes%60} ${requireContext().getString(R.string.minutes).toLowerCase()}", TextView.BufferType.NORMAL)
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
                        setupWorkSessionsFromToday()
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