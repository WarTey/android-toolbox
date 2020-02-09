package fr.isen.guillaume.androidtoolbox.recycler.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guillaume.androidtoolbox.R
import fr.isen.guillaume.androidtoolbox.model.Contact

class RecyclerAdapter(private val contacts: ArrayList<Contact>, private val context: Context) :
    RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_contacts_items,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bindContact(contacts[position], context)
    }
}