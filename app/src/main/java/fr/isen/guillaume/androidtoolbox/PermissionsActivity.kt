package fr.isen.guillaume.androidtoolbox

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_permissions.*

class PermissionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        imgGallery.setOnClickListener { getImageSource() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHOOSER -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data?.extras?.get("data") == null)
                        imgGallery.setImageURI(data?.data)
                    else
                        imgGallery.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
        }
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAPTURE_REQUEST -> {
                if (grantResults.isNotEmpty() && !permissions.isNullOrEmpty() && permissions[0] == CAPTURE_PERMISSIONS[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[1] == CAPTURE_PERMISSIONS[1] && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    takePicture()
                else
                    Toast.makeText(this, R.string.error_permissions, Toast.LENGTH_LONG).show()
            }
        }
    }*/

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

    /*private fun checkPermissions(permissions: Array<String>): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
                return false
        }
        return true
    }*/

    companion object {
        private const val CHOOSER = 1
    }
}
