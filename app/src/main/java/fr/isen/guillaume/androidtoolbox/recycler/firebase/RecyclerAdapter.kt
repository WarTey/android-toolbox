package fr.isen.guillaume.androidtoolbox.recycler.firebase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guillaume.androidtoolbox.R

class RecyclerAdapter(private val messages: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<FirebaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        return FirebaseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_firebase_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        holder.bindMessage(messages[position], context)
    }

    override fun getItemCount() = messages.size
}