package fr.isen.guillaume.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import fr.isen.guillaume.androidtoolbox.recycler.firebase.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_firebase_data.*

class FirebaseDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_data)

        val messages = ArrayList<String>()
        val databaseReference = FirebaseDatabase.getInstance().getReference("message")

        messageListener(databaseReference, messages)
        imgSend.setOnClickListener { addMessage(databaseReference, messages.count()) }
    }

    private fun messageListener(messageReference: DatabaseReference, messages: ArrayList<String>) {
        messageReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                    getData(p0, false, messages)
            }
        })

        messageReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                    getData(p0, true, messages)
            }
        })
    }

    private fun getData(
        dataSnapshot: DataSnapshot,
        isRefreshed: Boolean,
        messages: ArrayList<String>
    ) {
        if (isRefreshed)
            while (messages.isNotEmpty())
                messages.removeAt(messages.count() - 1)

        for (data in dataSnapshot.children) {
            val message = data.value.toString()
            if (message.isNotEmpty())
                messages.add(message)
        }
        setRecyclerView(isRefreshed, messages)
    }

    private fun setRecyclerView(isRefreshed: Boolean, messages: ArrayList<String>) {
        if (isRefreshed)
            recyclerFirebase.adapter?.notifyDataSetChanged()
        else {
            recyclerFirebase.layoutManager = LinearLayoutManager(this@FirebaseDataActivity)
            recyclerFirebase.addItemDecoration(
                DividerItemDecoration(
                    this@FirebaseDataActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            recyclerFirebase.adapter = RecyclerAdapter(messages, this)
        }
    }

    private fun addMessage(databaseReference: DatabaseReference, count: Int) {
        databaseReference.child((count + 1).toString())
            .setValue(txtSend.text.toString())
    }
}
