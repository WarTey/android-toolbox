package fr.isen.guillaume.androidtoolbox

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.guillaume.androidtoolbox.model.Contact
import fr.isen.guillaume.androidtoolbox.recycler.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_permissions.*


class PermissionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        getContacts()
        imgGallery.setOnClickListener { getImageSource() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHOOSER -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data?.extras?.get(CAPTURE_DATA) == null)
                        imgGallery.setImageURI(data?.data)
                    else
                        imgGallery.setImageBitmap(data.extras?.get(CAPTURE_DATA) as Bitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_REQUEST -> {
                if (grantResults.isNotEmpty() && !permissions.isNullOrEmpty() && permissions[0] == CONTACTS_PERMISSIONS[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getContacts()
                else
                    Toast.makeText(this, R.string.error_permissions, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getImageSource() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"

        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_source))
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent)
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        startActivityForResult(chooser, CHOOSER)
    }

    private fun getContacts() {
        if (checkPermissions(CONTACTS_PERMISSIONS, CONTACTS_REQUEST)) {
            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            val contacts = ArrayList<Contact>()
            cursor?.let {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val phoneNumber = getContactPhone(id)
                        val email = getContactEmail(id)

                        val contentImg = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
                        val img = Uri.withAppendedPath(contentImg, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)

                        contacts.add(Contact(name, phoneNumber, email, img))
                    }
                    recyclerPermissions.layoutManager = LinearLayoutManager(this)
                    recyclerPermissions.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                    recyclerPermissions.adapter = RecyclerAdapter(contacts)
                } else
                    Toast.makeText(this, getString(R.string.no_contacts), Toast.LENGTH_SHORT).show()
                cursor.close()
            }
        }
    }

    private fun getContactPhone(id: String): String? {
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null)
        cursor?.let {
            while (cursor.moveToNext())
                return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            cursor.close()
        }
        return null
    }

    private fun getContactEmail(id: String): String? {
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id, null, null)
        cursor?.let {
            while (cursor.moveToNext())
                return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
            cursor.close()
        }
        return null
    }

    private fun checkPermissions(permissions: Array<String>, requestCode: Int): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
                return false
            }
        }
        return true
    }

    companion object {
        private val CONTACTS_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
        private const val CONTACTS_REQUEST = 1000
        private const val CHOOSER = 1
        private const val CAPTURE_DATA = "data"
    }
}
