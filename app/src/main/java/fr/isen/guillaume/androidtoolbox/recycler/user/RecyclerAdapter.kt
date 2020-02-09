package fr.isen.guillaume.androidtoolbox.recycler.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guillaume.androidtoolbox.R
import fr.isen.guillaume.androidtoolbox.model.user.User

class RecyclerAdapter(private val users: ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_webservices_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUser(users[position])
    }

    override fun getItemCount() = users.size
}