package com.smarttoolfactory.todoplus.data

import androidx.room.Query
import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TasksDataSource {

    /**
     * Select all tasks from the tasks table. This method returns a [Flowable] which
     * emits values as long as observable changes or insert/ delete operation is invoked
     *
     * @return all tasks.
     */
    fun getTasks(): Flowable<List<Task>>

    /**
     * Select all tasks with query keyword that match title, description or tags.
     * This method returns a [Flowable] which emits values as long as
     * observable changes or insert/ delete operation is invoked
     *
     * @return all tasks that match the query
     */
    fun getTasks(taskQuery: String): Flowable<List<Task>>


    /**
     * Select all tasks with query keyword that match title, description or tags
     */
    fun getTasksOnce(): Single<List<Task>>

    /**
     * Select all tasks with query keyword that match title, description or tags, and return
     * query results only once
     */
    fun getTasksOnce(taskQuery: String): Single<List<Task>>

    fun getTask(taskId: String): Single<Task>

    fun saveTask(task: Task): Completable

    fun completeTask(task: Task): Completable

    fun completeTask(taskId: String): Completable

    fun activateTask(task: Task): Completable

    fun activateTask(taskId: String): Completable

    fun clearCompletedTasks(): Completable

    fun deleteAllTasks(): Completable

    fun deleteTask(taskId: String): Completable

    fun deleteTask(task: Task): Completable

}