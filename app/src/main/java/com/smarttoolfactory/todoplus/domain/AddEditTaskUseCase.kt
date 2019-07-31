package com.smarttoolfactory.todoplus.domain

import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditTaskUseCase @Inject constructor(private val repository: TasksRepository) : BaseUseCase() {


    fun saveTask(task: Task): Completable {

        val isEmpty = task.isEmpty
        val locationSet = task.locationSet
        val dueDateSet = task.dueDateSet

        if (!isEmpty && locationSet && dueDateSet) {
            return repository.saveTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    println("AddEditTaskUseCase clearCompletedTasks() Completable doOnDispose()")
                }
        } else {
            return Completable.error(Exception("Empty Fi"))
        }

    }

    fun deleteTask(taskId: Long): Completable {
        return repository.deleteTask(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose {
                println("AddEditTaskUseCase deleteTask() Completable doOnDispose()")
            }
    }

    fun getTaskById(taskId: Long): Single<Task> {
        return repository.getTask(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose {
                println("AddEditTaskUseCase deleteTask() Completable doOnDispose()")
            }
    }

    override fun dispose() {

    }

}