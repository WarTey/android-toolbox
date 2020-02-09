package fr.isen.guillaume.androidtoolbox

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.muddzdev.styleabletoast.StyleableToast
import fr.isen.guillaume.androidtoolbox.database.BackupDatabase
import fr.isen.guillaume.androidtoolbox.model.Backup
import fr.isen.guillaume.androidtoolbox.model.BackupRoom
import fr.isen.guillaume.androidtoolbox.security.DataSecurity
import fr.isen.guillaume.androidtoolbox.thread.WorkerThread
import kotlinx.android.synthetic.main.activity_backup.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupActivity : AppCompatActivity() {

    private val workerThread = WorkerThread(THREAD_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        val fileJson = cacheDir.absolutePath + "/users.json"
        val fileSecure = cacheDir.absolutePath + "/users.txt"
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val backupDatabase = BackupDatabase.getInstance(this)

        setDateField()
        navigationChoice()
        workerThread.start()

        btnSave.setOnClickListener { writeData(false, fileJson, sharedPreferences) }
        btnSeeSave.setOnClickListener { getData(false, fileJson, sharedPreferences) }
        btnSecureSave.setOnClickListener { writeData(true, fileSecure, sharedPreferences) }
        btnSecureSeeSave.setOnClickListener { getData(true, fileSecure, sharedPreferences) }
        btnSaveDb.setOnClickListener { insertBackup(backupDatabase) }
        btnSeeDb.setOnClickListener { fetchBackup(backupDatabase) }
    }

    private fun isValidDate(date: Long): Boolean {
        return if (date <= Calendar.getInstance().timeInMillis)
            true
        else {
            StyleableToast.makeText(
                this,
                getString(R.string.invalid_date),
                Toast.LENGTH_LONG,
                R.style.StyleToast
            ).show()
            false
        }
    }

    private fun setDateField() {
        txtBirthday.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.show(supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                if (isValidDate(it))
                    txtBirthday.setText(SimpleDateFormat(DATE_PATTERN, Locale.FRANCE).format(it))
            }
        }
    }

    private fun navigationChoice() {
        bottomNavigationBackup.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itemClassic -> {
                    hideBtn()
                    btnSave.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.translation_y_one
                        )
                    )
                    btnSave.visibility = View.VISIBLE
                    btnSeeSave.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.translation_y_two
                        )
                    )
                    btnSeeSave.visibility = View.VISIBLE
                    true
                }
                R.id.itemCrypt -> {
                    hideBtn()
                    btnSecureSave.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.translation_y_one
                        )
                    )
                    btnSecureSave.visibility = View.VISIBLE
                    btnSecureSeeSave.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.translation_y_two
                        )
                    )
                    btnSecureSeeSave.visibility = View.VISIBLE
                    true
                }
                R.id.itemBase -> {
                    hideBtn()
                    btnSaveDb.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.translation_y_one
                        )
                    )
                    btnSaveDb.visibility = View.VISIBLE
                    btnSeeDb.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.translation_y_two
                        )
                    )
                    btnSeeDb.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
    }

    private fun hideBtn() {
        btnSave.visibility = View.INVISIBLE
        btnSecureSave.visibility = View.INVISIBLE
        btnSeeSave.visibility = View.INVISIBLE
        btnSecureSeeSave.visibility = View.INVISIBLE
        btnSaveDb.visibility = View.INVISIBLE
        btnSeeDb.visibility = View.INVISIBLE
    }

    private fun isEmptyFields(name: String, firstname: String, birthday: String): Boolean {
        return name.isEmpty() || firstname.isEmpty() || birthday.isEmpty()
    }

    private fun writeData(isSecure: Boolean, file: String, sharedPreferences: SharedPreferences) {
        if (!isEmptyFields(
                txtName.text.toString(),
                txtFirstname.text.toString(),
                txtBirthday.text.toString()
            )
        ) {
            if (isSecure) {
                val fileData = Gson().toJson(
                    Backup(
                        txtName.text.toString(),
                        txtFirstname.text.toString(),
                        txtBirthday.text.toString()
                    )
                )
                val encodedData =
                    DataSecurity().encrypt(sharedPreferences, fileData.toByteArray(Charsets.UTF_8))
                File(file).writeBytes(encodedData)
            } else
                File(file).writeText(
                    Gson().toJson(
                        Backup(
                            txtName.text.toString(),
                            txtFirstname.text.toString(),
                            txtBirthday.text.toString()
                        )
                    )
                )
        } else
            viewBadInput()
    }

    private fun getData(isSecure: Boolean, filePath: String, sharedPreferences: SharedPreferences) {
        if (File(filePath).exists()) {
            val backup: Backup
            backup = if (isSecure) {
                val data =
                    DataSecurity().decrypt(sharedPreferences, DataSecurity().readFile(filePath))
                Gson().fromJson(data, Backup::class.java)
            } else
                Gson().fromJson(
                    File(filePath).bufferedReader().use { it.readText() },
                    Backup::class.java
                )
            showPopup(backup)
        } else
            readError()
    }

    private fun insertBackup(backupDatabase: BackupDatabase?) {
        if (!isEmptyFields(
                txtName.text.toString(),
                txtFirstname.text.toString(),
                txtBirthday.text.toString()
            )
        ) {
            val backup = BackupRoom()
            backup.name = txtName.text.toString()
            backup.firstname = txtFirstname.text.toString()
            backup.birthday = txtBirthday.text.toString()
            val task = Runnable { backupDatabase?.backupDao()?.insert(backup) }
            workerThread.postTask(task)
        } else
            viewBadInput()
    }

    private fun fetchBackup(backupDatabase: BackupDatabase?) {
        val task = Runnable {
            val backup = backupDatabase?.backupDao()?.getLastUser()
            Handler().post {
                if (backup == null)
                    readError()
                else
                    showPopup(Backup(backup.name, backup.firstname, backup.birthday))
            }
        }
        workerThread.postTask(task)
    }

    private fun viewBadInput() {
        if (txtName.text.isNullOrEmpty())
            inputName.error = getString(R.string.empty_input)
        else
            inputName.error = null

        if (txtFirstname.text.isNullOrEmpty())
            inputFirstname.error = getString(R.string.empty_input)
        else
            inputFirstname.error = null

        if (txtBirthday.text.isNullOrEmpty())
            inputDate.error = getString(R.string.empty_input)
        else
            inputDate.error = null
    }

    private fun showPopup(backup: Backup) {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.my_infos)).setMessage(
            getString(
                R.string.alert_saved_infos,
                backup.firstname,
                backup.name,
                backup.birthday,
                backup.getAge()
            )
        ).setPositiveButton(getString(R.string.ok_btn), null).show()
    }

    private fun readError() {
        StyleableToast.makeText(
            this,
            getString(R.string.error_database_read),
            Toast.LENGTH_LONG,
            R.style.StyleToast
        ).show()
    }

    override fun onDestroy() {
        BackupDatabase.destroyInstance()
        workerThread.quit()
        super.onDestroy()
    }

    companion object {
        private const val PREFS_NAME = "KeyToolBox"
        private const val DATE_PATTERN = "dd-MM-yyyy"
        private const val THREAD_NAME = "WorkerThread"
    }
}
