package com.towerowl.workin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
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


class FragmentOverview : Fragment() {

    private lateinit var toggleButton: AppCompatButton
    private lateinit var sessionList: RecyclerView

    private val mAdapter: SessionAdapter = SessionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_overview, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupButtons()
        setupRecycler()
        setupStreams()
    }


    private fun setupViews() {
        toggleButton = view?.findViewById<AppCompatButton>(R.id.f_overview_toggle_session) ?: throw Exception()
        sessionList = view?.findViewById<RecyclerView>(R.id.f_overview_activity_recycler) ?: throw Exception()
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
                    is WorkSessionEvent.Updated  -> { mAdapter.update(it.data) }
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