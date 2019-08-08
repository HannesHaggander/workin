package com.towerowl.workin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.towerowl.workin.R
import com.towerowl.workin.data.WorkSession
import java.text.SimpleDateFormat
import java.util.*

class SessionAdapter : RecyclerView.Adapter<SessionViewHolder>() {

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
        data.asSequence()
            .find { it.id == item.id }
            ?.let {
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

class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(data: WorkSession) {
        val dformat = SimpleDateFormat("hh:mm", Locale.getDefault())

        itemView.findViewById<AppCompatTextView>(R.id.vh_session_start).text =
            "Start: ${dformat.format(data.createdAt.time)}"
        itemView.findViewById<AppCompatTextView>(R.id.vh_session_end).text =
            "End: ${data.closedAt?.let { dformat.format(it.time) } ?: ""}"
    }
}