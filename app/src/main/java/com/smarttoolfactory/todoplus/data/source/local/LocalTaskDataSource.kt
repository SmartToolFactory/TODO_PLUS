package com.smarttoolfactory.todoplus.data.source.local

import com.smarttoolfactory.todoplus.data.TasksDataSource
import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTaskDataSource @Inject constructor(val tasksDao: TasksDao) : TasksDataSource {

    override fun getTasks(): Flowable<List<Task>> {
        return tasksDao.getTasks()
    }

    override fun getTasks(taskQuery: String): Flowable<List<Task>> {
        return tasksDao.getTasks(taskQuery)
    }

    override fun getTasksOnce(): Single<List<Task>> {
        return tasksDao.getTasksOnce()
    }

    override fun getTasksOnce(taskQuery: String): Single<List<Task>> {
        return tasksDao.getTasksOnce(taskQuery)
    }

    override fun getTask(taskId: Long): Single<Task> {
        return tasksDao.getTaskById(taskId)
    }


    override fun saveTask(task: Task): Completable {
        return tasksDao.insert(task)
    }

    override fun completeTask(task: Task): Completable {
        return tasksDao.updateCompleted(task.id, completed = true)
    }

    override fun completeTask(taskId: Long): Completable {
        return tasksDao.updateCompleted(taskId, completed = true)
    }

    override fun activateTask(task: Task): Completable {
        return tasksDao.updateCompleted(task.id, false)
    }

    override fun activateTask(taskId: Long): Completable {
        return tasksDao.updateCompleted(taskId, completed = false)
    }

    override fun clearCompletedTasks(): Completable {
        return tasksDao.deleteCompletedTasks()
    }

    override fun deleteAllTasks(): Completable {
        return tasksDao.deleteTasks()
    }

    override fun deleteTask(taskId: Long): Completable {
        return tasksDao.deleteTaskById(taskId)
    }

    override fun deleteTask(task: Task): Completable {
        return tasksDao.delete(task)
    }

}