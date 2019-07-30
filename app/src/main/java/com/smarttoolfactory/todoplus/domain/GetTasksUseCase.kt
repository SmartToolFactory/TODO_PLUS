package com.smarttoolfactory.todoplus.domain

import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.tasks.TasksFilterType
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This UseCase is only responsible of retrieving data from DB as Flowable or Single. Data returned
 * from db is filtered and might be queried via search function
 */
class GetTasksUseCase @Inject constructor(private val repository: TasksRepository) : BaseUseCase() {


    /**
     * Select all tasks from the tasks table. This method returns a [Flowable] which
     * emits values as long as observable changes or insert/ delete operation is invoked
     *
     * Force boolean causes task to be refreshed by remote or local source.
     * Otherwise data from cache is used.
     *
     * 🔥 This method does not query database with a keyword, uses RxJava2 filter method to filter
     * after data is received.
     *
     * @return all tasks.
     */
    fun getTasks(forceUpdate: Boolean = false, currentFilter: BehaviorProcessor<TasksFilterType>, userQuery: BehaviorProcessor<String>): Flowable<List<Task>> {

        // !!! TODO Use SELECTIVE query instead of filtering query

        return Flowable
                .combineLatest(repository.getTasks(forceUpdate), currentFilter, userQuery, Function3 { listTask: List<Task>, queryFilter: TasksFilterType, query: String ->
                    listTask
                            // Filter via task type
                            .filter {
                                when (queryFilter) {
                                    TasksFilterType.ALL_TASKS -> true
                                    TasksFilterType.ACTIVE_TASKS -> it.isActive
                                    TasksFilterType.COMPLETED_TASKS -> it.isCompleted
                                }
                            }

                            .filter {
                                // Check if keyword entered by user matches title, descriptio or tag
                                it.title.startsWith(query) || it.description.startsWith(query) || it.tag.startsWith(query)
                            }

                })

                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * Select all tasks with query keyword that match title, description or tags.
     * This method returns a [Flowable] which emits values as long as
     * observable changes or insert/ delete operation is invoked
     *
     * Force boolean causes task to be refreshed by remote or local source.
     * Otherwise data from cache is used.
     *
     * @return all tasks that match the query
     */
    fun getTasksQuery(forceUpdate: Boolean = false, taskQuery: BehaviorProcessor<String>, filter: BehaviorProcessor<TasksFilterType>): Flowable<List<Task>> {

        var filter2 = TasksFilterType.ALL_TASKS

        return Flowable

                .combineLatest(taskQuery, filter, BiFunction { t1: String, t2: TasksFilterType ->
                    filter2 = t2
                    t1
                })

                // Do not filter if query is empty if not look for at least 3 letters
                .filter {
                    if (it == null || it == "") {
                        true
                    } else {
                        it.length > 2
                    }
                }

                .switchMap {
                    // Decide which query
                    val flowable = if (it == null || it == "") {
                        repository.getTasks(forceUpdate)

                    } else {
                        repository.getTasks(forceUpdate, it)
                    }


                    flowable.map {
                        it.filter {
                            when (filter2) {
                                TasksFilterType.ALL_TASKS -> true
                                TasksFilterType.ACTIVE_TASKS -> it.isActive
                                TasksFilterType.COMPLETED_TASKS -> it.isCompleted
                            }
                        }
                    }
                }

                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    }


    /**
     * Select all tasks with query keyword that match title, description or tags
     *
     * Force boolean causes task to be refreshed by remote or local source.
     * Otherwise data from cache is used.
     *
     * @return all tasks
     */
    fun getTasksOnce(forceUpdate: Boolean = false, taskQuery: Observable<String>, filter: Observable<TasksFilterType>): Observable<List<Task>> {

        return Observable.combineLatest(taskQuery, filter, BiFunction { t1: String, t2: TasksFilterType ->
            repository.getTasksOnce(forceUpdate, t1)
        }).flatMap {
            it.toObservable()
        }


    }


    fun test(): Observable<List<Task>> {
        return Observable.create {

        }
    }


    override fun dispose() {

    }

}