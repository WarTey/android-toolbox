package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        imgLifeCycle.setOnClickListener { startActivity(Intent(this, LifeCycleActivity::class.java)) }
        imgLogout.setOnClickListener { logout() }
        imgBackup.setOnClickListener { startActivity(Intent(this, BackupActivity::class.java)) }
        imgPermissions.setOnClickListener { startActivity(Intent(this, PermissionsActivity::class.java)) }
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
