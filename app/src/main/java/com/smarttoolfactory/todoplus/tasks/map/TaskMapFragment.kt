package com.smarttoolfactory.todoplus.tasks.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.databinding.FragmentMapBinding
import com.smarttoolfactory.todoplus.tasks.TaskListViewModel
import javax.inject.Inject

class TaskMapFragment : BaseMapFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var taskListViewModel: TaskListViewModel

    private lateinit var dataBinding: FragmentMapBinding

    private lateinit var listTask: List<Task>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        taskListViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(TaskListViewModel::class.java)

        dataBinding =
                DataBindingUtil.inflate<FragmentMapBinding>(inflater, R.layout.fragment_map, container, false)

//        dataBinding.viewmodel = taskListViewModel

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListViewModel.items.observe(this, Observer {
            listTask = it
        })

    }

    override fun initMap(map: GoogleMap) {

    }

    companion object {

        fun newInstance(): TaskMapFragment {

            val args = Bundle()

            val fragment = TaskMapFragment()
            fragment.arguments = args
            return fragment
        }
    }

}