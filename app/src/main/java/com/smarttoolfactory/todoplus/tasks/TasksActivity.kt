package com.smarttoolfactory.todoplus.tasks

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.addedittask.AddEditTaskActivity
import com.smarttoolfactory.todoplus.databinding.ActivityMainBinding
import com.smarttoolfactory.todoplus.tasks.adapter.TaskFragmentStatePagerAdapter
import com.smarttoolfactory.todoplus.tasks.list.TaskListFragment
import com.smarttoolfactory.todoplus.tasks.map.TaskMapFragment
import dagger.android.support.DaggerAppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        taskListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskListViewModel::class.java)

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

        // Set up Floating Action Button

        dataBinding.fabAddTask.setOnClickListener {
            addNewTask()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tasks_menu, menu)

        // SearchView
        val searchItem = menu.findItem(R.id.action_search)
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
                R.id.menu_clear -> {
                    taskListViewModel.clearCompletedTasks()
                    true
                }
                R.id.menu_filter -> {
                    showFilteringPopUpMenu()
                    true
                }
                R.id.menu_refresh -> {
                    taskListViewModel.loadTasks(true)
                    true
                }
                else -> false
            }

    /**
     * Shows a popup menu that is used to filter tasks by all, active or completed
     */
    private fun showFilteringPopUpMenu() {
        val view = findViewById<View>(R.id.action_search)

        PopupMenu(this@TasksActivity, view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {

                taskListViewModel.setFiltering(
                        when (it.itemId) {
                            R.id.active -> TasksFilterType.ACTIVE_TASKS
                            R.id.completed -> TasksFilterType.COMPLETED_TASKS
                            else -> TasksFilterType.ALL_TASKS
                        }
                )
                taskListViewModel.loadTasks(false)
                true
            }

            show()
        }
    }

    /**
     * Opens add/edit screen for user to add new task
     */
    private fun addNewTask() {

        val intent = Intent(this, AddEditTaskActivity::class.java)
        // start your next activity
        startActivity(intent)
    }


}
