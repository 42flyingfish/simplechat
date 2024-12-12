package com.example.simplechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatMessageAdapter(private val dataSet: MutableList<Message>) :
    RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            // TODO("Implement the thing")
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
        return dataSet.size
    }

}