package fr.isen.guillaume.androidtoolbox.recycler.user

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.guillaume.androidtoolbox.model.user.User
import kotlinx.android.synthetic.main.recyclerview_webservices_items.view.*

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindUser(user: User) {
        val name = user.name.first + " " + user.name.last
        val address = user.location.street.number + " " + user.location.state + " " + user.location.street.name + " " + user.location.city
        itemView.txtName.text = name
        itemView.txtMail.text = user.email
        itemView.txtAddress.text = address
        Picasso.get().load(user.picture.large).into(itemView.imgRandom)
    }
}