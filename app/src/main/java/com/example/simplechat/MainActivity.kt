package com.example.simplechat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mData : DatabaseReference
    private lateinit var mDataWatch: DatabaseReference
    private lateinit var adapter: ChatMessageAdapter
    private val messages = mutableListOf<Message>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser == null)
            startActivity(Intent(this, SigninActivity::class.java))

        mData = Firebase.database.reference
        mDataWatch = FirebaseDatabase.getInstance().getReference("messages")
        val database = Firebase.database

        adapter = ChatMessageAdapter(messages)
        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = adapter

        mDataWatch.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
                val newMessage =  snapshot.getValue(Message::class.java)
                if (newMessage == null) {
                    Log.d("adChildEventListener", "The newMessage was null")
                } else {
                    messages.add(newMessage)
                    adapter.notifyItemInserted(messages.size-1)
                    binding.recycle.scrollToPosition(messages.size-1)
                    Log.d("onChildAdded", "onChildAdded: ${previousChildName}")
                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedKey = snapshot.key
                val index = messages.indexOfFirst { it.key == removedKey }
                messages.removeAt(index)
                adapter.notifyItemRemoved(index)
                if (messages.size > 0) binding.recycle.scrollToPosition(messages.size-1)

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
                // There is no need to reorder unless I want to do something.
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val changedKey = snapshot.key
                val newMessage = snapshot.getValue(Message::class.java)
                val index = messages.indexOfFirst { it.key == changedKey }
                messages.elementAt(index).text= newMessage?.text
                messages.elementAt(index).displayName = newMessage?.displayName
                adapter.notifyItemChanged(index)
            }

            override fun onCancelled(error: DatabaseError) {
                    Log.w("onCanceled", error.toString())
            }
        })




        binding.sendButton.setOnClickListener {
            if (binding.messageText.text.toString() != ""){

                // Reference
                val reference = database.reference.child("messages").push()

                // Prepare the message
                val myMessage = Message(
                    text = binding.messageText.text.toString(),
                    displayName = mAuth.currentUser?.email.toString(),
                    key = reference.key
                )

                // Actually set the values
                reference.setValue(myMessage)
                binding.messageText.setText("")
            }
        }
    }
}