package fr.isen.guillaume.androidtoolbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_firebase_login.*

class FirebaseLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_login)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null)
            goToData()

        btnSend.setOnClickListener { login(auth) }
        btnNew.setOnClickListener { goToRegister() }
    }

    private fun login(auth: FirebaseAuth) {
        if (!isEmptyInput()) {
            auth.signInWithEmailAndPassword(
                txtUsername.text.toString(),
                txtPassword.text.toString()
            ).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    goToData()
                    viewLoginSuccess()
                } else
                    viewLoginError()
            }
        } else
            viewBadInput()
    }

    private fun isEmptyInput(): Boolean {
        return txtUsername.text.isNullOrEmpty() || txtPassword.text.isNullOrEmpty()
    }

    private fun resetInputError() {
        inputUsername.error = null
        inputPassword.error = null
    }

    private fun viewLoginError() {
        resetInputError()
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.connection_error))
            .setMessage(getString(R.string.connection_error_message))
            .setPositiveButton(getString(R.string.ok_btn), null).show()
    }

    private fun viewLoginSuccess() {
        StyleableToast.makeText(
            this,
            getString(R.string.login_success),
            Toast.LENGTH_LONG,
            R.style.StyleToastSuccess
        ).show()
    }

    private fun viewBadInput() {
        resetInputError()

        if (txtUsername.text.isNullOrEmpty())
            inputUsername.error = getString(R.string.empty_input)

        if (txtPassword.text.isNullOrEmpty())
            inputPassword.error = getString(R.string.empty_input)
    }

    private fun goToRegister() {
        val intentRegister = Intent(this, FirebaseRegisterActivity::class.java)
        intentRegister.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentRegister)
        finish()
    }

    private fun goToData() {
        val intentData = Intent(this, FirebaseDataActivity::class.java)
        intentData.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentData)
        finish()
    }
}
