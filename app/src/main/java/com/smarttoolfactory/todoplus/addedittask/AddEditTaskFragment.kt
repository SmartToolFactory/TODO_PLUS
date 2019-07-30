package com.smarttoolfactory.todoplus.addedittask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.databinding.FragmentAddEditTaskBinding
import dagger.android.support.DaggerFragment
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 *  TODO: I got some issues with Data-Binding in this fragment will check again
 */
class AddEditTaskFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var addEditTaskViewModel: AddEditTaskViewModel

    private lateinit var dataBinding: FragmentAddEditTaskBinding

    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        addEditTaskViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AddEditTaskViewModel::class.java)

        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_edit_task, container, false
        )

        dataBinding.viewmodel = addEditTaskViewModel

        // 🔥 This is required if LiveData is used for data-binding
        dataBinding.lifecycleOwner = this


//        println("AddEditTaskFragment onCreateView() $this")
        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }


    private fun bindViews() {
        dataBinding.llDueDate.setOnClickListener {
            openDateAndTimePicker()
        }

        dataBinding.llLocation.setOnClickListener {
            (activity as AddEditTaskActivity).openLocationPicker()
        }


        // TODO Handle with Data-binding
        addEditTaskViewModel.isLocationSet.observe(this, Observer {

            if (it == true) {
                dataBinding.ivRemoveLocation.visibility = View.VISIBLE
                dataBinding.tvLocation.text =
                    "${addEditTaskViewModel.task.value?.latitude},${addEditTaskViewModel.task.value?.longitude}"

            } else {
                dataBinding.ivRemoveLocation.visibility = View.GONE
                dataBinding.tvLocation.text = getString(R.string.select_location)
            }
        })

        addEditTaskViewModel.isDueDateSet.observe(this, androidx.lifecycle.Observer {

            if (it == true) {
                dataBinding.ivRemoveDueDate.visibility = View.VISIBLE
                val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
                dataBinding.tvDueDate.text = sdf.format(addEditTaskViewModel.task.value?.dueDate)

            } else {
                dataBinding.ivRemoveDueDate.visibility = View.GONE
                dataBinding.tvDueDate.text = getString(R.string.select_due_date)
            }
        })

        // Check if task save is successful
        addEditTaskViewModel.isSaveTaskSuccess.observe(this, androidx.lifecycle.Observer {

            // TODO 🔥🔥🔥 With Live Data this is triggered twice???
            counter++
            println("AddEditTaskFragment isSaveTaskSuccess() observe $counter")

            if (it == false) {
                Toast.makeText(activity, "Please fill fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Task is saved", Toast.LENGTH_SHORT).show()
                goBack()
            }
        })
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

        val calendar: Calendar = Calendar.getInstance()

        val yearCalendar = calendar.get(Calendar.YEAR)
        val monthCalendar = calendar.get(Calendar.MONTH)
        val dayCalendar = calendar.get(Calendar.DAY_OF_MONTH)
        val hourCalendar = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteCalendar = calendar.get(Calendar.MINUTE)

        val onTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val requestCode = Random().nextInt()
            addEditTaskViewModel.setDueDate(calendar.timeInMillis, requestCode)

            (activity as AddEditTaskActivity).setAlarm(calendar.timeInMillis, requestCode)

        }

        val onDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(activity, onTimeSetListener, hourCalendar, minuteCalendar, true).show()

        }

        val datePicker = DatePickerDialog(
            activity, onDateSetListener, yearCalendar, monthCalendar, dayCalendar
        )

        datePicker.datePicker.minDate = System.currentTimeMillis()

        datePicker.show()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_add_edit_done) {

//            addEditTaskViewModel.task.value?.apply {
//
//                title = dataBinding.etTitle.text.toString()
//                description = dataBinding.etDescription.text.toString()
//
//            }
            addEditTaskViewModel.saveTask(addEditTaskViewModel.task.value!!)
        }

        return true
    }


    private fun goBack() {
        activity?.onBackPressed()
    }


}