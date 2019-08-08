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
import com.towerowl.workin.data.WorkSession
import com.towerowl.workin.data.WorkSessionRepository
import com.towerowl.workin.events.WorkSessionEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class FragmentOverview : Fragment() {

    private val mStartButton: AppCompatButton by lazy { view?.findViewById<AppCompatButton>(R.id.f_overview_sign_in) ?: throw Exception() }
    private val mStopButton: AppCompatButton by lazy { view?.findViewById<AppCompatButton>(R.id.f_overview_sign_out) ?: throw Exception() }
    private val mSessionList: RecyclerView by lazy { view?.findViewById<RecyclerView>(R.id.f_overview_activity_recycler) ?: throw Exception() }

    private val mAdapter: SessionAdapter = SessionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        setupRecycler()
    }

    private fun getWorkSessionRepo(): WorkSessionRepository =
        App.get(requireActivity())
            .workSessionRepo

    private fun setupButtons() {
        mStartButton.setOnClickListener {
            val session = WorkSession(UUID.randomUUID(), "Injected ${Random().nextInt(1000)}")

            getWorkSessionRepo().insertWorkSession(session)
                .also { getWorkSessionRepo().setOpenWorkSession(session) }
        }

        mStopButton.setOnClickListener {
            getWorkSessionRepo().closeOpenWorkSession()
        }

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
        mSessionList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        mSessionList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        mSessionList.adapter = mAdapter
    }

    internal class SessionAdapter : RecyclerView.Adapter<SessionViewHolder>() {
        private val data: MutableList<WorkSession> = mutableListOf()
        private val vhMap = mutableMapOf<WorkSession, SessionViewHolder>()

        fun add(item: WorkSession) {
            if (data.any { it.id == item.id }) {
                return
            }
            data.add(item)
            notifyItemInserted(itemCount - 1)
        }

        fun remove(item: WorkSession) {
            val pos = data.indexOf(item)
            if (pos < 0) {
                return
            }
            data.removeAt(pos)
            notifyItemRemoved(pos)
        }

        fun update(item: WorkSession) {
            data.asSequence().find { it.id == item.id }?.let {
                vhMap[it]?.setData(item)
                val pos = data.indexOf(it)
                data[pos] = item
                notifyItemChanged(pos)
            }
        }

        fun clear() {
            data.clear()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
            return SessionViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_session_data, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
            holder.setData(data[position])
            vhMap[data[position]] = holder
        }

    }

    internal class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(data: WorkSession) {
            val dformat = SimpleDateFormat("hh:mm", Locale.getDefault())

            itemView.findViewById<AppCompatTextView>(R.id.vh_session_start).text =
                "Start: ${dformat.format(data.createdAt.time)}"
            itemView.findViewById<AppCompatTextView>(R.id.vh_session_end).text =
                "End: ${data.closedAt?.let { dformat.format(it.time) } ?: ""}"
        }
    }
}