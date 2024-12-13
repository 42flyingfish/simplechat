package com.example.simplechat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatMessageAdapter(private val dataSet: MutableList<Message>) :
    RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val messageTextView: TextView = itemView.findViewById(R.id.mesageBox)
            val senderTextView: TextView = itemView.findViewById(R.id.senderBox)

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ChatMessageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.message_box, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ChatMessageAdapter.ViewHolder, position: Int) {
        holder.messageTextView.text = dataSet[position].text.toString()
        holder.senderTextView.text = dataSet[position].displayName.toString()
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount", "Item count is ${dataSet.size}")
        return dataSet.size
    }

    // Update the adapter with new data

    fun updateMessages(newMessages: MutableList<Message>) {
        Log.d("Database Replacement", "updateMessages  The dataSet contained: ${dataSet.size}")
        Log.d("Database Replacement", "updateMessages The newMessages contained: ${newMessages.size}")
        Log.d("Database Replacement", "updateMessages The newMessages contained: ${newMessages}")
        if (newMessages.isEmpty()) {
            Log.d("updateMessages", "updateMessages The newMesages is somehow empty prior to clearing the db")
            return
        }
        dataSet.clear()
        //val success = dataSet.addAll(newMessages)
//        if (!success) {
//            Log.d("Database Replacement", "updateMessages The dataset failed to change")
//        }
        if (newMessages.isEmpty()) {
            Log.d("updateMessages", "updateMessages The newMesages is somehow empty after calling clear")
            return
        }
        dataSet.add(newMessages.first())
        Log.d("Database Replacement", "udateMessages The dataSet now has : ${dataSet.size}")
        notifyDataSetChanged()
    }

    fun clearList(newMessages: MutableList<Message>) {
        newMessages.clear()
    }

    fun forceRefresh() {
        notifyDataSetChanged()
    }


}