package com.smarttoolfactory.todoplus.addedittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.domain.AddEditTaskUseCase
import javax.inject.Inject

/**
 * ViewModel for adding or deleting tasks
 *
 * TODO
 */
class AddEditTaskViewModel @Inject
constructor(
    private val repository: TasksRepository,
    private val addEditUseCase: AddEditTaskUseCase
) : ViewModel() {

    // Current task to be saved or deleted
    private var _task = MutableLiveData<Task>()
//        .apply {
//            value = Task()
//        }

    val task: LiveData<Task> = _task

    // ??? Transformations.map not working?
    /**
     * Used for data-binding to check if a location is set to task
     */
    val isLocationChanged: LiveData<Boolean> = Transformations.map(_task) { input: Task? ->
        println("😎 AddEditTaskViewModel isLocationChanged Transformed")
        input?.locationSet ?: true
    }

    /**
     * Used for data-binding to check if a location is set to task
     */
    val isDueDateChanged: LiveData<Boolean> = Transformations.map(_task) { input: Task? ->
        println("😎 AddEditTaskViewModel isDueDateChanged Transformed")
        input?.dueDateSet ?: true
    }


    val isDueDateSet = MutableLiveData<Boolean>()
    val isLocationSet = MutableLiveData<Boolean>()
    /**
     * Check if save task success
     *
     * 🔥🔥 ??? Observed Multiple times if this is a [MutableLiveData]
     */
    val isSaveTaskSuccess = SingleLiveEvent<Boolean>()
//    val title = MutableLiveData<String>()
//    val description = MutableLiveData<String>()

    fun saveTask(task: Task) {
        addEditUseCase.saveTask(task)
            .subscribe(
                {
                    isSaveTaskSuccess.value = true
//                    _task.value = null
                },
                {
                    isSaveTaskSuccess.value = false
                    println("🥺 AddEditTaskViewModel onError $isSaveTaskSuccess.value ")

                })
    }

    fun deleteTask(task: Task) {
        addEditUseCase.deleteTask(task)
            .subscribe(
                {
                    _task.value = null
                },
                {

                })
    }

    fun getTaskById(taskId : Int) {

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


        newTask.title = "Hello World"
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