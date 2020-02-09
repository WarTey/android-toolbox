package fr.isen.guillaume.androidtoolbox.recycler.firebase

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guillaume.androidtoolbox.R
import kotlinx.android.synthetic.main.recyclerview_firebase_items.view.*

class FirebaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindMessage(message: String, context: Context) {
        itemView.txtMessage.text = context.getString(R.string.message_hint, message)
    }
}