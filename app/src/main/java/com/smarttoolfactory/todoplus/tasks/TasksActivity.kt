package com.smarttoolfactory.todoplus.tasks

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.smarttoolfactory.todoplus.addedittask.AddEditTaskActivity
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.databinding.ActivityMainBinding
import com.smarttoolfactory.todoplus.tasks.adapter.TaskFragmentStatePagerAdapter
import com.smarttoolfactory.todoplus.tasks.list.TaskListFragment
import com.smarttoolfactory.todoplus.tasks.map.TaskMapFragment
import dagger.android.support.DaggerAppCompatActivity
import java.util.*

import javax.inject.Inject


class TasksActivity : DaggerAppCompatActivity() {

    /**
     * ViewModelFactory provides ViewModels from a map that were injected via Map annotation
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * ViewModel that contains list of task and queries via Repository or UseCases
     */
    private lateinit var taskListViewModel: TaskListViewModel


    private lateinit var dataBinding: ActivityMainBinding

    /**
     * Check if app is starting for the first time. If so, populate DB
     */
    private var isFistStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, com.smarttoolfactory.todoplus.R.layout.activity_main)

        taskListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskListViewModel::class.java)

        // 🔥 This is required if LiveData is used for data-binding
        dataBinding.lifecycleOwner = this

        bindViews()


        /*
          Method for popluating db with fake data
        */
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isFistStart = sharedPreferences.getBoolean(getString(com.smarttoolfactory.todoplus.R.string.first_start), true)

        if (isFistStart) {
            populateDB()
            val editor = sharedPreferences.edit()
            editor.putBoolean(getString(com.smarttoolfactory.todoplus.R.string.first_start), false)
            editor.apply()
        }

    }


    /**
     * Set toolbar and create fragments and set UI related actions and elements
     */
    private fun bindViews() {

        // Set Toolbar
        val toolbar = dataBinding.toolbar
        setSupportActionBar(toolbar)

        // Set up ViewPager

        val taskListFragment = TaskListFragment.newInstance()
        val taskMapFragment = TaskMapFragment.newInstance()

        // TODO ViewPager2 Alpha not working well for now
//        val viewPager2 = dataBinding.viewPager
//
//        val viewPagerFragmentAdapter = ViewPager2FragmentAdapter(fragmentManager = supportFragmentManager, lifecycle = lifecycle)
//
//        // Add fragments to FragmentStateAdapter and set adapter of viewPager2
//        viewPagerFragmentAdapter.apply {
//            addFragment(taskListFragment)
//            addFragment(taskMapFragment)
//            viewPager2.adapter = this
//        }

        val viewPager = dataBinding.viewPager

        val taskFragmentStatePagerAdapter = TaskFragmentStatePagerAdapter(supportFragmentManager)

        taskFragmentStatePagerAdapter.apply {
            addFragment(taskListFragment, "List")
            addFragment(taskMapFragment, "Map")
            viewPager.adapter = this
        }

        // Set up Tabs
        val tabLayout = dataBinding.tabs
        tabLayout.setupWithViewPager(viewPager)

        subscribeUI()

    }

    /**
     * Method for listening UI changes by user interaction
     */
    private fun subscribeUI() {
        // Set up Floating Action Button

        dataBinding.fabAddTask.setOnClickListener {
            addNewTask()
        }

        taskListViewModel.openEditTaskEvent.observe(this, Observer {
            editTask(it)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.smarttoolfactory.todoplus.R.menu.tasks_menu, menu)

        // SearchView
        val searchItem = menu.findItem(com.smarttoolfactory.todoplus.R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Title, Description or Tags"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                taskListViewModel.performTaskQuery(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        // TODO Set icon visibility other than SearchView or Add SearchFragment
//        val menuItemFilter = menu.findItem(R.id.menu_filter)
//
////        SearchView open
//        searchView.setOnSearchClickListener {
//            menuItemFilter.setVisible(false)
//        }
//
////         SearchView closed
//        searchView.setOnCloseListener {
//            menuItemFilter.setVisible(true)
//            false
//        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            com.smarttoolfactory.todoplus.R.id.menu_clear -> {
                taskListViewModel.clearCompletedTasks()
                true
            }
            com.smarttoolfactory.todoplus.R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            com.smarttoolfactory.todoplus.R.id.menu_refresh -> {
                taskListViewModel.loadTasks(true)
                true
            }
            else -> false
        }

    /**
     * Shows a popup menu that is used to filter tasks by all, active or completed
     */
    private fun showFilteringPopUpMenu() {
        val view = findViewById<View>(com.smarttoolfactory.todoplus.R.id.action_search)

        PopupMenu(this@TasksActivity, view).run {
            menuInflater.inflate(com.smarttoolfactory.todoplus.R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {

                taskListViewModel.setFiltering(
                    when (it.itemId) {
                        com.smarttoolfactory.todoplus.R.id.active -> TasksFilterType.ACTIVE_TASKS
                        com.smarttoolfactory.todoplus.R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    }
                )
                taskListViewModel.loadTasks(false)
                true
            }

            show()
        }
    }

    fun editTask(taskId: Long) {
        val intent = Intent(this, AddEditTaskActivity::class.java)
        intent.putExtra(AddEditTaskActivity.TASK_BUNDLE, taskId)
        // start your next activity
        startActivity(intent)
    }

    /**
     * Opens add/edit screen for user to add new task
     */
    private fun addNewTask() {

        val intent = Intent(this, AddEditTaskActivity::class.java)
        // start your next activity
        startActivity(intent)
    }

    /**
     * Mock Method
     */
    private fun populateDB() {

        val titleList = arrayOf("Work", "Hobby", "Shopping", "Travel", "Vacation", "Business", "Voyage")
        val description = arrayOf(
            "Go to work",
            "Play a game",
            "Buy apple",
            "Travel somewhere",
            "Rest and refresh",
            "Earn money",
            "Visit moon"
        )

        val addressList = arrayOf("Istanbul", "Berlin", "Paris", "London", "Amsterdam", "Helsinki", "Madrid")

        val random = Random()

        val latLng = LatLng(41.01384, 28.94966)
        val location = Location("Dummy")
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude


        repeat(100) {

            val customLocation = getLocationInLatLngRad(2_000_000.0, location)

            println("TasksActivity Lat: ${customLocation.latitude}, Lon: ${customLocation.longitude}")

            val task = Task(
                title = titleList[random.nextInt(7)],
                description = description[random.nextInt(7)],
                latitude = customLocation.latitude,
                longitude = customLocation.longitude,
                locationSet = true,
                address = addressList[random.nextInt(7)]
            )

            taskListViewModel.saveTask(task)
        }

    }

    private fun getLocationInLatLngRad(radiusInMeters: Double, currentLocation: Location): Location {
        val x0 = currentLocation.longitude
        val y0 = currentLocation.latitude

        val random = Random()

        // Convert radius from meters to degrees.
        val radiusInDegrees = radiusInMeters / 111320f

        // Get a random distance and a random angle.
        val u = random.nextDouble()
        val v = random.nextDouble()
        val w = radiusInDegrees * Math.sqrt(u)
        val t = 2.0 * Math.PI * v
        // Get the x and y delta values.
        val x = w * Math.cos(t)
        val y = w * Math.sin(t)

        // Compensate the x value.
        val new_x = x / Math.cos(Math.toRadians(y0))

        val foundLatitude: Double
        val foundLongitude: Double

        foundLatitude = y0 + y
        foundLongitude = x0 + new_x

        val copy = Location(currentLocation)
        copy.latitude = foundLatitude
        copy.longitude = foundLongitude
        return copy
    }

}
