package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        welcomeMessage()

        imgLifeCycle.setOnClickListener { startActivity(Intent(this, LifeCycleActivity::class.java)) }
        imgLogout.setOnClickListener { logout() }
        imgBackup.setOnClickListener { startActivity(Intent(this, BackupActivity::class.java)) }
        imgPermissions.setOnClickListener { startActivity(Intent(this, PermissionsActivity::class.java)) }
        imgWebservices.setOnClickListener { startActivity(Intent(this, WebservicesActivity::class.java)) }
    }

    private fun welcomeMessage() {
        Snackbar.make(layoutHome, getString(R.string.welcome_name, intent.getStringExtra("username")), Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimaryDark)).show()
    }

    private fun logout() {
        val editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
        goToLogin()
    }

    private fun goToLogin() {
        val intentLogin = Intent(this, LoginActivity::class.java)
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentLogin)
        finish()
    }

    companion object {
        private const val PREFS_NAME = "LoginToolBox"
    }
}
