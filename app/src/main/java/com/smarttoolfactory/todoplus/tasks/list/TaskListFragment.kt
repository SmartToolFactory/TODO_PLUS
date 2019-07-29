package com.smarttoolfactory.todoplus.tasks.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.databinding.FragmentTasksBinding
import com.smarttoolfactory.todoplus.tasks.TaskListViewModel
import com.smarttoolfactory.todoplus.tasks.adapter.TasksAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TaskListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var taskListViewModel: TaskListViewModel

    private lateinit var dataBinding: FragmentTasksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        taskListViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(TaskListViewModel::class.java)


        dataBinding =
                DataBindingUtil.inflate<FragmentTasksBinding>(inflater, R.layout.fragment_tasks, container, false)

        dataBinding.viewmodel = taskListViewModel

        return dataBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        dataBinding.lifecycleOwner = this.viewLifecycleOwner

        setTaskListAdapter()
    }

    private fun setTaskListAdapter() {
        dataBinding.recyclerView.adapter = TasksAdapter(taskListViewModel)

    }

    companion object {

        fun newInstance(): TaskListFragment {

            val args = Bundle()

            val fragment = TaskListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}