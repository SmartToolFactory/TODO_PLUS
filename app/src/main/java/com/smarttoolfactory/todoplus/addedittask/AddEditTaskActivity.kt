package com.smarttoolfactory.todoplus.addedittask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowId
import androidx.core.app.AlarmManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.AlarmReceiver
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.databinding.ActivityAddEditTaskBinding
import dagger.android.support.DaggerAppCompatActivity
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

    private var editTaskId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editTaskId = intent.getIntExtra(TASK_BUNDLE, -1)

        dataBinding =
            DataBindingUtil.setContentView(this, com.smarttoolfactory.todoplus.R.layout.activity_add_edit_task)

        addEditTaskViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditTaskViewModel::class.java)
        addEditTaskViewModel.getTaskById(editTaskId)
            bindViews()

        if (savedInstanceState == null) {
            setNavigation(ADD_TASK)
        }


        supportFragmentManager.addOnBackStackChangedListener {
           val backStackEntryCount = supportFragmentManager.backStackEntryCount

           val fragmentsSize = supportFragmentManager.fragments.size

            val currentFragment = supportFragmentManager.findFragmentById(com.smarttoolfactory.todoplus.R.id.content_frame)

            println("🏪 AddEditTaskActivity onCreate() backStackEntryCount: $backStackEntryCount, fragmentsSize: $fragmentsSize, currentFragment: $currentFragment")
        }
    }


    /**
     * Set toolbar and create fragments and set UI related actions and elements
     */
    private fun bindViews() {

        // Set Toolbar
        val toolbar = dataBinding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return false
    }


    /**
     * Open map fragment to add Location
     */
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
            // Set up Add/Edit task fragment

            var fragment: AddEditTaskFragment? =
                supportFragmentManager.findFragmentById(com.smarttoolfactory.todoplus.R.id.content_frame) as? AddEditTaskFragment

            if (fragment == null) {
                fragment = AddEditTaskFragment.newInstance()

                supportFragmentManager
                    .beginTransaction()
//                    .addToBackStack(null)
                    .replace(com.smarttoolfactory.todoplus.R.id.content_frame, fragment)
                    .commit()
            }

        } else {

            // Set up Add location fragment

            var fragment: AddLocationFragment? =
                supportFragmentManager.findFragmentById(com.smarttoolfactory.todoplus.R.id.content_frame) as? AddLocationFragment

            if (fragment == null) {
                fragment = AddLocationFragment.newInstance()

                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(com.smarttoolfactory.todoplus.R.id.content_frame, fragment)
                    .commit()
            }

        }
    }

    companion object {
        const val TASK_BUNDLE = "TASK_BUNDLE"
        const val ADD_TASK = 0
        const val ADD_LOCATION = 1
    }

}

