package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initLayout()

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString(
                getString(R.string.key_username),
                ""
            ).equals(USERNAME) && sharedPreferences.getString(
                getString(R.string.key_password),
                ""
            ).equals(PASSWORD)
        )
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
        if (isValidCredentials()) {
            setSharedPreferences(sharedPreferences)
            goToHome()
        } else
            viewBadInput()
    }

    private fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString(getString(R.string.key_username), txtUsername.text.toString())
        editor.putString(getString(R.string.key_password), txtPassword.text.toString())
        editor.apply()
    }

    private fun isValidCredentials(): Boolean {
        return txtUsername.text.toString() == USERNAME && txtPassword.text.toString() == PASSWORD
    }

    private fun viewBadInput() {
        if (txtUsername.text.toString() != USERNAME)
            inputUsername.error = getString(R.string.incorrect_username)
        else
            inputUsername.error = null

        if (txtPassword.text.toString() != PASSWORD)
            inputPassword.error = getString(R.string.incorrect_password)
        else
            inputPassword.error = null
    }

    private fun goToHome() {
        val intentHome = Intent(this, HomeActivity::class.java)
        intentHome.putExtra(getString(R.string.key_username), USERNAME)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentHome)
        finish()
    }

    private fun viewLoginInfos() {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.password_forget))
            .setMessage(getString(R.string.password_retrieve, USERNAME, PASSWORD))
            .setPositiveButton(getString(R.string.ok_btn), null).show()
    }

    companion object {
        private const val PREFS_NAME = "LoginToolBox"
        private const val USERNAME = "admin"
        private const val PASSWORD = "123"
    }
}
