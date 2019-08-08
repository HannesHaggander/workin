package com.towerowl.workin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.towerowl.workin.data.WorkSessionRepository
import com.towerowl.workin.events.WorkSessionEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class FragmentOverview : Fragment() {

    private lateinit var startButton: Lazy<AppCompatButton>
    private lateinit var stopButton: Lazy<AppCompatButton>
    private lateinit var sessionList: Lazy<RecyclerView>

    private val mAdapter: SessionAdapter = SessionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupButtons()
        setupRecycler()
        setupStreams()
    }

    private fun setupViews() {
        startButton = lazy { view?.findViewById<AppCompatButton>(R.id.f_overview_sign_in) ?: throw Exception() }
        stopButton = lazy { view?.findViewById<AppCompatButton>(R.id.f_overview_sign_out) ?: throw Exception() }
        sessionList = lazy { view?.findViewById<RecyclerView>(R.id.f_overview_activity_recycler) ?: throw Exception() }
    }

    private fun getWorkSessionRepo(): WorkSessionRepository =
        App.get(requireActivity())
            .workSessionRepo

    private fun setupButtons() {
        startButton.value.setOnClickListener {
            val session = WorkSession(UUID.randomUUID(), "Injected ${Random().nextInt(1000)}")

            getWorkSessionRepo().insertWorkSession(session)
                .also { getWorkSessionRepo().setOpenWorkSession(session) }
        }

        stopButton.value.setOnClickListener {
            getWorkSessionRepo().closeOpenWorkSession()
        }
    }

    private fun setupStreams(){
        App.get(requireActivity())
            .workSessionRepo
            .messageFlow
            .doOnNext {
                when (it) {
                    is WorkSessionEvent.Inserted -> { mAdapter.add(it.data) }
                    is WorkSessionEvent.Removed  -> { mAdapter.remove(it.data) }
                    is WorkSessionEvent.Updated  -> { mAdapter.update(it.data) }
                }
            }
            .observeOn(Schedulers.newThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun setupRecycler() {
        sessionList.value.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        sessionList.value.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        sessionList.value.adapter = mAdapter
    }
}