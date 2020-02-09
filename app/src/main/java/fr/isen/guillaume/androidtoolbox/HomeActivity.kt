package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        welcomeProcess()

        materialCardLifeCycle.setOnClickListener { startActivity(Intent(this, LifeCycleActivity::class.java)) }
        materialCardBackup.setOnClickListener { startActivity(Intent(this, BackupActivity::class.java)) }
        materialCardPermissions.setOnClickListener { startActivity(Intent(this, PermissionsActivity::class.java)) }
        materialCardWebservices.setOnClickListener { startActivity(Intent(this, WebservicesActivity::class.java)) }
        materialCardFirebase.setOnClickListener { startActivity(Intent(this, FirebaseLoginActivity::class.java)) }
        materialCardAgenda.setOnClickListener { agendaMessage() }
        btnLogout.setOnClickListener { logout() }
    }

    private fun welcomeProcess() {
        txtUsername.text = intent.getStringExtra(getString(R.string.key_username))
        materialCardLifeCycle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_one))
        materialCardBackup.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_one))
        materialCardPermissions.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_two))
        materialCardWebservices.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_two))
        materialCardAgenda.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_three))
        materialCardFirebase.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_three))
        btnLogout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation_y_four))
    }

    private fun logout() {
        val editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
        goToLogin()

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null)
            auth.signOut()
    }

    private fun agendaMessage() {
        StyleableToast.makeText(this, getString(R.string.under_construction), Toast.LENGTH_LONG, R.style.StyleToast).show()
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
