package fr.isen.guillaume.androidtoolbox.recycler.contact

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.guillaume.androidtoolbox.R
import fr.isen.guillaume.androidtoolbox.model.Contact
import kotlinx.android.synthetic.main.recyclerview_contacts_items.view.*

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindContact(contact: Contact, context: Context) {
        itemView.nameContact.text = context.getString(
            R.string.name_hint,
            returnForEmptyField(context, contact.name, contact.name.isEmpty())
        )
        itemView.numberContact.text = context.getString(
            R.string.number_hint,
            returnForEmptyField(context, contact.number, contact.number.isNullOrEmpty())
        )
        itemView.emailContact.text = context.getString(
            R.string.email_hint,
            returnForEmptyField(context, contact.email, contact.email.isNullOrEmpty())
        )
        Picasso.get().load(contact.picture).error(R.drawable.no_picture).into(itemView.imgContact)
    }

    private fun returnForEmptyField(context: Context, text: String?, boolean: Boolean): String? {
        return if (boolean)
            context.getString(R.string.no_data)
        else
            text
    }
}