package com.smarttoolfactory.todoplus.domain

import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditTaskUseCase @Inject constructor(private val repository: TasksRepository) : BaseUseCase() {


    fun saveTask(task: Task): Completable {
        return repository.saveTask(task).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    println("AddEditTaskUseCase clearCompletedTasks() Completable doOnDispose()")
                }
    }

    override fun dispose() {

    }

}