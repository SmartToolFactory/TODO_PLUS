package com.smarttoolfactory.todoplus.addedittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.domain.AddEditTaskUseCase
import javax.inject.Inject

class AddEditTaskViewModelBackup @Inject
constructor(
    private val repository: TasksRepository,
    private val addEditUseCase: AddEditTaskUseCase
) : ViewModel() {

    // Current task to be saved or deleted
    private var _task = MutableLiveData<Task>().apply {
        value = Task()
    }
    val task: LiveData<Task> = _task


//    /**
//     * Used for data-binding to check if a location is set to task
//     */
//    val isLocationSet: LiveData<Boolean> = Transformations.map(_task) { input: Task? ->
//        println("😎 AddEditTaskViewModel isLocationSet Transformed")
//        input?.locationSet ?: true
//    }
//
//    /**
//     * Used for data-binding to check if a location is set to task
//     */
//    val isDueDateSet: LiveData<Boolean> = Transformations.map(_task) { input: Task? ->
//        println("😎 AddEditTaskViewModel isLocationSet Transformed")
//        input?.dueDateSet ?: true
//    }

    val isDueDateSet = MutableLiveData<Boolean>().apply { false }
    val isLocationSet = MutableLiveData<Boolean>().apply { false }

    /**
     * Check if save task success
     */

    val isSaveTaskSuccess = MutableLiveData<Boolean>().apply { false }


    // Two-way databinding, exposing MutableLiveData
    val title = MutableLiveData<String>()

    // Two-way databinding, exposing MutableLiveData
    val description = MutableLiveData<String>()


    fun saveTask(task: Task) {
        addEditUseCase.saveTask(task)
            .subscribe(
                {
                    isSaveTaskSuccess.value = true
                    _task.value = null
                },
                {
                    isSaveTaskSuccess.value = false
                })
    }

    fun deleteTask(taskId : Long) {
        addEditUseCase.deleteTask(taskId)
            .subscribe(
                {
                    _task.value = null
                },
                {

                })
    }

    /**
     * Set location of the Task
     */
    fun setLocation(latLng: LatLng, address: String) {
        var newTask = task.value?.copy()
        if (newTask == null) {
            newTask = Task()
        }
        newTask.locationSet = true
        newTask.latitude = latLng.latitude
        newTask.longitude = latLng.longitude
        newTask.address = address

        _task.value = newTask

        isLocationSet.value = true

    }

    fun setDueDate(timeInMillis: Long, requestCode: Int) {

        var newTask = task.value?.copy()
        if (newTask == null) {
            newTask = Task()
        }

        newTask.dueDateSet = true
        newTask.dueDateRequestCode = requestCode
        newTask.dueDate = timeInMillis

        _task.value = newTask

        isDueDateSet.value = true
    }

    fun removeDueDate() {
        isDueDateSet.value = false
    }

    fun removeLocation() {
        isLocationSet.value = false
    }

}