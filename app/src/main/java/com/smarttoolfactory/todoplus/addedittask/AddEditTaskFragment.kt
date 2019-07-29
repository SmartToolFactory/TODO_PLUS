package com.smarttoolfactory.todoplus.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.databinding.FragmentTasksBinding
import com.smarttoolfactory.todoplus.tasks.list.TaskListFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AddEditTaskFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var addEditTaskViewModel: AddEditTaskViewModel

    private lateinit var dataBinding: FragmentTasksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addEditTaskViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AddEditTaskViewModel::class.java)


        dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_task, container, false)

//        dataBinding.viewmodel = addEditTaskViewModel

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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