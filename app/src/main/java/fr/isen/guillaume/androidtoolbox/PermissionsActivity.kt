package fr.isen.guillaume.androidtoolbox

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muddzdev.styleabletoast.StyleableToast
import fr.isen.guillaume.androidtoolbox.model.Contact
import fr.isen.guillaume.androidtoolbox.model.Coordinate
import fr.isen.guillaume.androidtoolbox.recycler.contact.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_permissions.*

class PermissionsActivity : AppCompatActivity(), LocationListener {

    private var locationManager: LocationManager? = null
    private var coordinate: Coordinate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        initRequest()
        firstVisit()
        imgGallery.setOnClickListener { getImageSource() }
        txtAccess.setOnClickListener { showHelpDialog() }
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
            REQUESTS -> {
                getPos()
                getContacts()
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
        if (isPermissionsGranted(CONTACTS_PERMISSIONS, getString(R.string.error_permissions))) {
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
                    recyclerPermissions.adapter = RecyclerAdapter(contacts, applicationContext)
                } else
                    StyleableToast.makeText(this, getString(R.string.no_contacts), Toast.LENGTH_LONG, R.style.StyleToast).show()
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

    @SuppressLint("MissingPermission")
    private fun getPos() {
        if (isPermissionsGranted(POS_PERMISSIONS, getString(R.string.error_permissions))) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager?.let {
                val services = arrayOf(it.isProviderEnabled(LocationManager.GPS_PROVIDER), it.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                val providers = arrayOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)

                if (!services[0] && !services[1])
                    StyleableToast.makeText(this, getString(R.string.location_not_activated), Toast.LENGTH_LONG, R.style.StyleToast).show()
                else {
                    for ((index, service) in services.withIndex()) {
                        if (service) {
                            it.requestLocationUpdates(providers[index], 5000, 0f, this)
                            comparePos(it.getLastKnownLocation(providers[index]))
                        }
                    }
                }
            }
        }
    }

    private fun comparePos(location: Location?) {
        if (location != null) {
            if (coordinate != null)
                coordinate?.accuracy?.let { if (it < location.accuracy) updateCoord(location) }
            else
                updateCoord(location)
        }
    }

    private fun updateCoord(location: Location?) {
        valLatitude.text = location?.latitude.toString()
        valLongitude.text = location?.longitude.toString()
        coordinate = Coordinate(location?.latitude, location?.longitude, location?.accuracy)
    }

    private fun initRequest() {
        if (!isPermissionsGranted(PERMISSIONS, null))
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUESTS)
        else {
            getPos()
            getContacts()
        }
    }

    private fun firstVisit() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!sharedPreferences.getString(getString(R.string.key_permissions), "").equals(VISITED)) {
            val editor = sharedPreferences.edit()
            editor.putString(getString(R.string.key_permissions), VISITED)
            editor.apply()
            showHelpDialog()
        }
    }

    private fun showHelpDialog() {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.first_visit)).setMessage(getString(R.string.visit_text)).setPositiveButton(getString(R.string.ok_btn), null).show()
    }

    private fun isPermissionsGranted(permissions: Array<String>, message: String?): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
                if (!message.isNullOrEmpty())
                    StyleableToast.makeText(this, message, Toast.LENGTH_LONG, R.style.StyleToast).show()
                return false
            }
        }
        return true
    }

    override fun onLocationChanged(location: Location?) { comparePos(location) }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { }

    override fun onProviderEnabled(provider: String?) { }

    override fun onProviderDisabled(provider: String?) { }

    override fun onStop() {
        super.onStop()
        locationManager?.removeUpdates(this)
    }

    companion object {
        private val CONTACTS_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
        private val POS_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private const val REQUESTS = 1000
        private const val CHOOSER = 1
        private const val CAPTURE_DATA = "data"
        private const val PREFS_NAME = "VisitToolBox"
        private const val VISITED = "Visited"
    }
}
