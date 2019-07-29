package com.smarttoolfactory.todoplus.addedittask

import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_task)

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

        // Set up Fragment
        val fragment = AddEditTaskFragment.newInstance()

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tasks_menu, menu)






        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem) =
//            when (item.itemId) {
//                R.id.menu_clear -> {
//                    taskListViewModel.clearCompletedTasks()
//                    true
//                }
//                R.id.menu_filter -> {
//                    showFilteringPopUpMenu()
//                    true
//                }
//                R.id.menu_refresh -> {
//                    taskListViewModel.loadTasks(true)
//                    true
//                }
//                else -> false
//            }


}
