package com.smarttoolfactory.todoplus.addedittask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.databinding.FragmentAddEditTaskBinding
import dagger.android.support.DaggerFragment
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddEditTaskFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var addEditTaskViewModel: AddEditTaskViewModel

    private lateinit var dataBinding: FragmentAddEditTaskBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addEditTaskViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AddEditTaskViewModel::class.java)


        dataBinding = DataBindingUtil.inflate(
            inflater, com.smarttoolfactory.todoplus.R.layout.fragment_add_edit_task, container, false
        )

        dataBinding.viewmodel = addEditTaskViewModel

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.ivRemoveDueDate.setOnClickListener {
            dataBinding.ivRemoveDueDate.visibility = View.INVISIBLE
        }

        dataBinding.llDueDate.setOnClickListener {
            openDateAndTimePicker()
        }

        dataBinding.llLocation.setOnClickListener {
            (activity as AddEditTaskActivity).openLocationPicker()
        }
    }

    fun initFields() {

    }

    companion object {

        fun newInstance(): AddEditTaskFragment {

            val args = Bundle()

            val fragment = AddEditTaskFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun openDateAndTimePicker() {

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())

        val calendar: Calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hourCalendar = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteCalendar = calendar.get(Calendar.MINUTE)

        val onTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            dataBinding.tvDueDate.text = sdf.format(calendar.time)

            addEditTaskViewModel.task.value?.dueDate = calendar.timeInMillis

            dataBinding.ivRemoveDueDate.visibility = View.VISIBLE

            val requestCode = 101
            (activity as AddEditTaskActivity).setAlarm(calendar.timeInMillis, requestCode)

        }

        val onDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(activity, onTimeSetListener, hourCalendar, minuteCalendar, true).show()

        }

        val datePicker = DatePickerDialog(
            activity, onDateSetListener, year, month, day
        )

        datePicker.datePicker.minDate = System.currentTimeMillis()

        datePicker.show()

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}