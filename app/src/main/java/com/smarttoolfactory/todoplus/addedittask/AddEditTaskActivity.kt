package com.smarttoolfactory.todoplus.addedittask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.AlarmReceiver
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.databinding.ActivityAddEditTaskBinding
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject


class AddEditTaskActivity : DaggerAppCompatActivity() {


    /**
     * ViewModelFactory provides ViewModels from a map that were injected via Map annotation
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * ViewModel that contains list of task and queries via Repository or UseCases
     */
    private lateinit var addEditTaskViewModel: AddEditTaskViewModel

    private lateinit var dataBinding: ActivityAddEditTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding =
            DataBindingUtil.setContentView(this, com.smarttoolfactory.todoplus.R.layout.activity_add_edit_task)

        addEditTaskViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditTaskViewModel::class.java)

        // This is required if LiveData is used for data-binding
        dataBinding.lifecycleOwner = this

        bindViews()

    }


    /**
     * Set toolbar and create fragments and set UI related actions and elements
     */
    private fun bindViews() {

        // Set Toolbar
        val toolbar = dataBinding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setNavigation(ADD_TASK)


    }


    //    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.add_edit_menu, menu)
//
//        return true
//    }
//
//
    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }



    private fun saveTask() {

    }

    private fun deleteTask() {

    }

    fun openLocationPicker() {
        setNavigation(ADD_LOCATION)
    }

    /**
     * Create an alarm to trigger on specified data. Alarm is unique for each requestCode. Using
     * same requestCodes override the previous alarm and request code is required to cancel this alarm.
     */
    fun setAlarm(timeMillis: Long, requestCode: Int) {

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@AddEditTaskActivity, requestCode, intent, 0)

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            timeMillis,
            pendingIntent
        )
    }

    /**
     * Cancel alarm for the selected [Task]
     */
    fun cancelAlarm(requestCode: Int) {

    }



    /**
     * Change fragments. Each fragment added to backStack
     */
    private fun setNavigation(id: Int) {

        if (id == ADD_TASK) {
            // Set up Fragments
            val fragment = AddEditTaskFragment.newInstance()

            supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, fragment)
                .commit()
        } else {
            // Set up Fragments
            val fragment = AddLocationFragment.newInstance()

            supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, fragment)
                .commit()
        }
    }

    /**
     * set back button behavior
     */
    override fun onBackPressed() {

        val backStackEntryCount = supportFragmentManager.backStackEntryCount

        if (backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    companion object {
        const val ADD_TASK = 0
        const val ADD_LOCATION = 1
    }

}

