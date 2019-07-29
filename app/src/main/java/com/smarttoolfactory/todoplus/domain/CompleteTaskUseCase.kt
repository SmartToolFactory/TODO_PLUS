package com.smarttoolfactory.todoplus.domain

import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(private val repository: TasksRepository) : BaseUseCase() {


    fun setCompleted(task: Task, completed: Boolean): Completable {

        val completable: Completable

        if (completed) {
            completable = repository.completeTask(task)
        } else {
            completable = repository.activateTask(task)
        }

        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    println("ViewModel completeTask() Completable doOnDispose()")
                }
    }

    override fun dispose() {

    }


}
