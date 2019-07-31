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

    // 🔥🔥🔥 ??? Transformations.map not working?
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


    /**
     * Live data to check if due date is set, this one sets visbility of remove duedate button
     */
    val isDueDateSet = MutableLiveData<Boolean>()
    /**
     * Live data to check if location is set, this one sets visbility of remove location button
     */
    val isLocationSet = MutableLiveData<Boolean>()

    /**
     * Check if save task success, and trigger an event that can be listend only once
     *
     * 🔥🔥 ??? Observed Multiple times if this is a [MutableLiveData]
     */
    val saveSuccessEvent = SingleLiveEvent<Boolean>()

    fun saveTask(task: Task) {
        addEditUseCase.saveTask(task)
            .subscribe(
                {
                    saveSuccessEvent.value = true
                },
                {
                    saveSuccessEvent.value = false
                    println("🥺 AddEditTaskViewModel saveTask() onError $saveSuccessEvent.value ")

                })
    }

    fun deleteTask(taskId: Long) {
        addEditUseCase.deleteTask(taskId)
            .subscribe(
                {
                    println("🗿 TaskListViewModel deleteTask() onComplete()")
                    refreshTask()
                },
                {
                    println("🗿 TaskListViewModel deleteTask() error() ${it.message}")
                    refreshTask()
                })
    }

    /**
     * Create a new [Task] instance and set values of depending [LiveData] instances
     */
    private fun refreshTask() {
        _task.value = Task()
        isDueDateSet.value = _task.value?.dueDateSet
        isLocationSet.value =  _task.value?.locationSet
    }

    /**
     * Get the task with specified id, if it does not exist create a new one
     */
    fun getTaskById(taskId: Long) {
        addEditUseCase.getTaskById(taskId)
            .subscribe(
                {
                    print("AddEditTaskViewModel getTaskById() task: $it")
                    _task.value = it

                },
                {
                    println("🥺 AddEditTaskViewModel getTaskById() takId $taskId, error: ${it.message}")
                    refreshTask()
                })

        isLocationSet.value = _task.value?.locationSet
        isDueDateSet.value = _task.value?.dueDateSet
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

    /**
     * Set due date of the Task
     */
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