package com.example.simplechat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
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



        Toast.makeText(baseContext,
            "You made it ${mAuth.currentUser?.email}",
            Toast.LENGTH_LONG).show()

        mData = Firebase.database.reference
        mDataWatch = FirebaseDatabase.getInstance().getReference("messages")
        val database = Firebase.database


        // Dummy messages to check the recycler
        messages.add(Message("what", "how.com"))
        messages.add(Message("woooot", "how.com"))
        messages.add(Message("fear me", "me.com"))

        adapter = ChatMessageAdapter(messages)
        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = adapter

        // Listen for changes in the database
        // Notice that the messages is passed by reference to the adapter.
        // We can update the adapter by updating messages directly and then
        // calling forceRefresh
        // This is not a good idea for encapsilation.
        mDataWatch.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // check before clearing
                Log.d("Messages", "addValueEventListener Fetched messages: $messages")
                // Clear the existing messages
                messages.clear()
                // Read messages from the database
                for (messageSnapshot in dataSnapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                Log.d("Messages", "addValueEventListener Fetched messages: $messages")
                // Update the adapter with the new messages
                Log.d("Database change", "addValueEventListener The dataset now contains ${messages.size}")
                //adapter.updateMessages(messages)
                adapter.forceRefresh()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Log.d("DBERROR", "onCancelled: ${databaseError.toString()}")
            }
        })






        binding.sendButton.setOnClickListener {
            val myMessage =  Message(
                text = binding.messageText.text.toString(),
                displayName = mAuth.currentUser?.email.toString()
            )
            database.reference.child("messages").push().setValue(myMessage)
            binding.messageText.setText("")

        }
    }
}