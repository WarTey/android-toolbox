package fr.isen.guillaume.androidtoolbox.recycler.contact

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.guillaume.androidtoolbox.R
import fr.isen.guillaume.androidtoolbox.model.Contact
import kotlinx.android.synthetic.main.recyclerview_contacts_items.view.*

class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindContact(contact: Contact) {
        itemView.txtName.text = contact.name
        itemView.txtNumber.text = contact.number
        itemView.txtEmail.text = contact.email
        Picasso.get().load(contact.picture).error(R.mipmap.ic_launcher_round).into(itemView.imgContact)
    }
}