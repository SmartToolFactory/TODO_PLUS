package com.smarttoolfactory.todoplus.data

import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * [TasksRepository] interface is used for implementing Repository pattern which provides
 * persistence layer.
 */
interface TasksRepository {


    /**
     * Select all tasks from the tasks table. This method returns a [Flowable] which
     * emits values as long as observable changes or insert/ delete operation is invoked
     *
     * Force boolean causes task to be refreshed by remote or local source.
     * Otherwise data from cache is used.
     *
     * @return all tasks.
     */
    fun getTasks(forceUpdate: Boolean = false): Flowable<List<Task>>

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
    fun getTasks(forceUpdate: Boolean = false, taskQuery: String): Flowable<List<Task>>


    /**
     * Select all tasks with query keyword that match title, description or tags
     *
     * Force boolean causes task to be refreshed by remote or local source.
     * Otherwise data from cache is used.
     *
     * @return all tasks
     */
    fun getTasksOnce(forceUpdate: Boolean = false): Single<List<Task>>

    /**
     * Select all tasks with query keyword that match title, description or tags, and return
     * query results only once.
     *
     * Force boolean causes task to be refreshed by remote or local source.
     * Otherwise data from cache is used.
     *
     *  @return all tasks that match the query
     *
     */
    fun getTasksOnce(forceUpdate: Boolean = false, taskQuery: String): Single<List<Task>>


    fun getTask(taskId: Long, forceUpdate: Boolean = false): Single<Task>

    fun saveTask(task: Task): Completable

    fun completeTask(task: Task): Completable

    fun completeTask(taskId: Long): Completable

    fun activateTask(task: Task): Completable

    fun activateTask(taskId: Long): Completable

    fun clearCompletedTasks(): Completable

    fun deleteAllTasks(): Completable

    fun deleteTask(taskId: Long): Completable

    fun deleteTask(task: Task): Completable

}