package com.smarttoolfactory.todoplus.tasks


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.todoplus.addedittask.SingleLiveEvent
import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.domain.ClearCompletedUseCase
import com.smarttoolfactory.todoplus.domain.CompleteTaskUseCase
import com.smarttoolfactory.todoplus.domain.GetTasksUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskListViewModel @Inject
constructor(private val repository: TasksRepository,
            private val getTasksUseCase: GetTasksUseCase,
            private val clearCompletedUseCase: ClearCompletedUseCase,
            private val compleTasksUseCase: CompleteTaskUseCase) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    private var currentFiltering = TasksFilterType.ALL_TASKS

    private val currentFilter = BehaviorProcessor.createDefault<TasksFilterType>(TasksFilterType.ALL_TASKS)
    private val userQuery = BehaviorProcessor.createDefault<String>("")


    private var tasks = MutableLiveData<List<Task>>().apply {
        value = emptyList()
    }
    /**
     * Live data that contains tasks. This livedata is also used for databinding with list via
     *   app:items=
     */
    val items: LiveData<List<Task>> = tasks

    /**
     * Key to listen for editing task
     */
    val openEditTaskEvent = SingleLiveEvent<Long>()

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(tasks) {
        println("👀 TaskListViewModel() Transformations.map()")

        it.isEmpty()
    }

    init {

        val disposable = getTasksUseCase.getTasksQuery(false, userQuery, currentFilter)
                .subscribe(
                        {
                            // onNext
                            nextValue: List<Task> ->
                            tasks.value = nextValue
                            println("🥶 TaskListViewModel() onNext() size: $nextValue.size")
                        },

                        {
                            // onError
                            error ->
                            tasks.value = emptyList()
                            println("🥶 TaskListViewMode() onError() error: ${error.message}")

                        }
                )

        compositeDisposable.add(disposable)

    }

    /**
     * Gets tasks depending on filtering and query type. Filtering includes ALL, ACTIVE, and COMPLETED tasks.
     * Query loads tasks that contain query word as title, description or as tags.
     */
    fun loadTasks(forceUpdate: Boolean) {
        println("TaskListViewModel loadTasks() $forceUpdate")

//        getTasksUseCase.getTasksOnce(false,)

    }

    /**
     * Query type defines which tasks should be retrieved that contains this query keyword in
     * title, description or tags.
     */
    fun performTaskQuery(query: String) {
        userQuery.onNext(query)
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be [TasksFilterType.ALL_TASKS],
     * [TasksFilterType.COMPLETED_TASKS], or
     * [TasksFilterType.ACTIVE_TASKS]
     */
    fun setFiltering(filterType: TasksFilterType) {
        currentFiltering = filterType
        currentFilter.onNext(currentFiltering)
        println("TaskListViewModel setFiltering $currentFiltering")
    }

    /**
     * Mark a [Task] as [TasksFilterType.COMPLETED_TASKS] or
     * [TasksFilterType.ACTIVE_TASKS] by setting completed flag
     *
     * ⚠️ Called by DataBinding
     */
    fun completeTask(task: Task, completed: Boolean) {

        val disposable = compleTasksUseCase.setCompleted(task, completed).subscribe(
                {
                    // Refresh list to show the new state
                    loadTasks(false)

                },
                {
                    println("ViewModel completeTask() Completable onError() ${it.message}")
                }
        )
    }

    /**
     * Deletes [Task]s that marked as completed
     */
    fun clearCompletedTasks() {

        val disposable = clearCompletedUseCase.clearCompleted()
                .subscribe(
                        {
                            println("ViewModel clearCompletedTasks() Completable onComplete()")
                        },
                        {
                            println("ViewModel clearCompletedTasks() Completable onError() ${it.message}")
                        }
                )
    }

    /**
     * Retrieves the data from remote source first by using a forceUpdate flag
     */
    fun refresh() {

    }

    /**
     * Opens details of the task with taskId
     *
     * ⚠️ Called by DataBinding
     */
    fun openTask(taskId: Long) {
        openEditTaskEvent.value = taskId
    }

    // TODO ADD THIS to ADD/EDIT Task,
    fun saveTask(task: Task) {
        repository.saveTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            println("🗿 TaskListViewModel saveTask() onComplete")
                        },
                        {
                            println("🗿 TaskListViewModel saveTask() onError() ${it.message}")
                        }
                )


    }

    // Use this method if CompositeDisposable is used inside ViewModel
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

