package fr.isen.guillaume.androidtoolbox

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.gson.Gson
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
        workerThread.start()

        btnSave.setOnClickListener { writeData(false, fileJson, sharedPreferences) }
        btnSeeSave.setOnClickListener { getData(false, fileJson, sharedPreferences) }
        btnSecureSave.setOnClickListener { writeData(true, fileSecure, sharedPreferences) }
        btnSecureSeeSave.setOnClickListener { getData(true, fileSecure, sharedPreferences) }
        btnSaveDb.setOnClickListener { insertBackup(backupDatabase) }
        btnSeeDb.setOnClickListener { fetchBackup(backupDatabase) }
    }

    private fun checkValidDate(date: Calendar): Boolean {
        return if (date.timeInMillis <= Calendar.getInstance().timeInMillis)
            true
        else {
            Toast.makeText(this, getString(R.string.invalid_date), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun setDateField() {
        val calendar = Calendar.getInstance()
        txtBirthday.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)
                if (checkValidDate(date))
                    txtBirthday.text = SimpleDateFormat(DATE_PATTERN, Locale.FRANCE).format(date.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun checkEmptyFields(name: String, firstname: String, birthday: String): Boolean {
        return name.isEmpty() || firstname.isEmpty() || birthday.isEmpty()
    }

    private fun writeData(isSecure: Boolean, file: String, sharedPreferences: SharedPreferences) {
        if (!checkEmptyFields(txtName.text.toString(), txtFirstname.text.toString(), txtBirthday.text.toString())) {
            if (isSecure) {
                val fileData = Gson().toJson(Backup(txtName.text.toString(), txtFirstname.text.toString(), txtBirthday.text.toString()))
                val encodedData = DataSecurity().encrypt(sharedPreferences, fileData.toByteArray(Charsets.UTF_8))
                File(file).writeBytes(encodedData)
            } else
                File(file).writeText(Gson().toJson(Backup(txtName.text.toString(), txtFirstname.text.toString(), txtBirthday.text.toString())))
        } else
            Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_LONG).show()
    }

    private fun getData(isSecure: Boolean, file: String, sharedPreferences: SharedPreferences) {
        val backup: Backup
        backup = if (isSecure) {
            val data = DataSecurity().decrypt(sharedPreferences, DataSecurity().readFile(file))
            Gson().fromJson(data, Backup::class.java)
        } else
            Gson().fromJson(File(file).bufferedReader().use { it.readText() }, Backup::class.java)
        showPopup(backup)
    }

    private fun insertBackup(backupDatabase: BackupDatabase?) {
        if (!checkEmptyFields(txtName.text.toString(), txtFirstname.text.toString(), txtBirthday.text.toString())) {
            val backup = BackupRoom()
            backup.name = txtName.text.toString()
            backup.firstname = txtFirstname.text.toString()
            backup.birthday = txtBirthday.text.toString()
            val task = Runnable { backupDatabase?.backupDao()?.insert(backup) }
            workerThread.postTask(task)
        } else
            Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_LONG).show()
    }

    private fun fetchBackup(backupDatabase: BackupDatabase?) {
        val task = Runnable {
            val backup = backupDatabase?.backupDao()?.getLastUser()
            Handler().post {
                if (backup == null)
                    Toast.makeText(this, getString(R.string.error_database_read), Toast.LENGTH_LONG).show()
                else
                    showPopup(Backup(backup.name, backup.firstname, backup.birthday))
            }
        }
        workerThread.postTask(task)
    }

    private fun showPopup(backup: Backup) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(getString(R.string.alert_saved_infos, backup.firstname, backup.name, backup.birthday, backup.getAge())).setTitle(getString(R.string.my_infos))
        alertDialog.create().show()
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
