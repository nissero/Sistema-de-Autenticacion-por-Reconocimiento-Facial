package com.biogin.myapplication.ui.rrhh.logs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biogin.myapplication.R
import com.biogin.myapplication.logs.Log

class LogsAdapter(val context : Context, val logList : List<Log>): RecyclerView.Adapter<LogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context)
            .inflate(R.layout.item_layout_logs_rrhh, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (logList != null && logList.size > 0){
            var log = logList.get(position)
            holder.timestamp.text = addLineJumpToStringInSpaces(log.timestamp)
            holder.logEventName.text = addLineJumpToStringInSpaces(log.logEventName.value)
            holder.dniMasterUser.text = log.dniMasterUser
            holder.dniUserAffected.text = log.dniUserAffected
            holder.userCategory.text = addLineJumpToStringInSpaces(log.userCategory)
        }
    }
    fun addLineJumpToStringInSpaces(s : String) : String {
        return s.replace("\\s+".toRegex(), "\n")
    }
    override fun getItemCount(): Int {
        return logList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestamp: TextView = itemView.findViewById(R.id.item_timestamp_log_rrhh)
        val logEventName: TextView = itemView.findViewById(R.id.item_log_type_rrrhh)
        val dniMasterUser: TextView = itemView.findViewById(R.id.item_master_user_dni_rrhh)
        val dniUserAffected: TextView = itemView.findViewById(R.id.item_dni_user_affected_rrhh)
        val userCategory: TextView = itemView.findViewById(R.id.item_user_category_rrrhh)

    }
}