package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString("username", "").equals(USERNAME) && sharedPreferences.getString("password", "").equals(PASSWORD))
            goToHome()

        btnSend.setOnClickListener { login(sharedPreferences) }
    }

    private fun login(sharedPreferences: SharedPreferences) {
        if (authCheck()) {
            setSharedPreferences(sharedPreferences)
            Toast.makeText(this, getString(R.string.welcome) + " " + txtUsername.text.toString(), Toast.LENGTH_LONG).show()
            goToHome()
        } else
            Toast.makeText(this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show()
    }

    private fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString("username", txtUsername.text.toString())
        editor.putString("password", txtPassword.text.toString())
        editor.apply()
    }

    private fun authCheck(): Boolean {
        return txtUsername.text.toString() == USERNAME && txtPassword.text.toString() == PASSWORD
    }

    private fun goToHome() {
        val intentHome = Intent(this, HomeActivity::class.java)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentHome)
        finish()
    }

    companion object {
        private const val PREFS_NAME = "LoginToolBox"
        private const val USERNAME = "admin"
        private const val PASSWORD = "123"
    }
}
