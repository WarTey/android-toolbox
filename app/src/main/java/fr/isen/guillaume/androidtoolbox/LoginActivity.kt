package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initLayout()

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString("username", "").equals(USERNAME) && sharedPreferences.getString("password", "").equals(PASSWORD))
            goToHome()

        btnSend.setOnClickListener { login(sharedPreferences) }
        btnForget.setOnClickListener { viewLoginInfos() }
    }

    private fun initLayout() {
        Handler().postDelayed({
            constraintLayoutInput.visibility = View.VISIBLE
            constraintLayoutConnection.visibility = View.VISIBLE
        }, 2500)
    }

    private fun login(sharedPreferences: SharedPreferences) {
        if (inputCheck()) {
            setSharedPreferences(sharedPreferences)
            Snackbar.make(layoutLogin, getString(R.string.welcome_name, txtUsername.text.toString()), Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimaryDark)).show()
            goToHome()
        } else
            viewBadInput()
    }

    private fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString("username", txtUsername.text.toString())
        editor.putString("password", txtPassword.text.toString())
        editor.apply()
    }

    private fun inputCheck(): Boolean {
        return txtUsername.text.toString() == USERNAME && txtPassword.text.toString() == PASSWORD
    }

    private fun viewBadInput() {
        if (txtUsername.text.toString() != USERNAME)
            inputUsername.error = "Identifiant incorrect"
        else
            inputUsername.error = null

        if (txtPassword.text.toString() != PASSWORD)
            inputPassword.error = "Mot de passe incorrect"
        else
            inputPassword.error = null
    }

    private fun goToHome() {
        val intentHome = Intent(this, HomeActivity::class.java)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentHome)
        finish()
    }

    private fun viewLoginInfos() {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.password_forget)).setMessage(getString(R.string.password_retrieve, USERNAME, PASSWORD)).setPositiveButton(getString(R.string.ok_btn), null).show()
    }

    companion object {
        private const val PREFS_NAME = "LoginToolBox"
        private const val USERNAME = "admin"
        private const val PASSWORD = "123"
    }
}
