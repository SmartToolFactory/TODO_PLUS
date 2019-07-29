package com.smarttoolfactory.todoplus.data

import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * [TasksRepositoryImpl] is concreate implementation of Repository pattern which acts as a Persistence
 * layer with local, remote and cached providers.
 */
class TasksRepositoryImpl @Inject constructor(val tasksDataSource: TasksDataSource) : TasksRepository {


    override fun getTasks(forceUpdate: Boolean): Flowable<List<Task>> {
        return tasksDataSource.getTasks()
    }

    override fun getTasks(forceUpdate: Boolean, taskQuery: String): Flowable<List<Task>> {
        return tasksDataSource.getTasks(taskQuery)
    }

    override fun getTasksOnce(forceUpdate: Boolean): Single<List<Task>> {
        return tasksDataSource.getTasksOnce()
    }

    override fun getTasksOnce(forceUpdate: Boolean, taskQuery: String): Single<List<Task>> {
        return tasksDataSource.getTasksOnce(taskQuery)
    }

    override fun getTask(taskId: String, forceUpdate: Boolean): Single<Task> {
        return tasksDataSource.getTask(taskId)
    }

    override fun saveTask(task: Task): Completable {
        return tasksDataSource.saveTask(task)
    }

    override fun completeTask(task: Task): Completable {
        return tasksDataSource.completeTask(task)
    }

    override fun completeTask(taskId: String): Completable {
        return tasksDataSource.completeTask(taskId)
    }

    override fun activateTask(task: Task): Completable {
        return tasksDataSource.activateTask(task)
    }

    override fun activateTask(taskId: String): Completable {
        return tasksDataSource.activateTask(taskId)
    }

    override fun clearCompletedTasks(): Completable {
        return tasksDataSource.clearCompletedTasks()
    }

    override fun deleteAllTasks(): Completable {
        return tasksDataSource.deleteAllTasks()
    }

    override fun deleteTask(taskId: String): Completable {
        return tasksDataSource.deleteTask(taskId)
    }

    override fun deleteTask(task: Task): Completable {
        return tasksDataSource.deleteTask(task.id)
    }


}