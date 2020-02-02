package fr.isen.guillaume.androidtoolbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSend.setOnClickListener { login() }
    }

    private fun login() {
        if (authCheck()) {
            Toast.makeText(this, getString(R.string.welcome) + " " + txtUsername.text.toString(), Toast.LENGTH_LONG).show()
            goToHome()
        } else
            Toast.makeText(this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show()
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
        private const val USERNAME = "admin"
        private const val PASSWORD = "123"
    }
}
