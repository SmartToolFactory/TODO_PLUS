package com.smarttoolfactory.todoplus.domain

import com.smarttoolfactory.todoplus.data.TasksRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * This UseCase is only responsible of deleting tasks that marked as completed
 */
class ClearCompletedUseCase @Inject constructor(private val repository: TasksRepository) : BaseUseCase() {

    fun clearCompleted(): Completable {

        return repository.clearCompletedTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    println("ClearCompletedUseCase clearCompletedTasks() Completable doOnDispose()")
                }
    }

    override fun dispose() {

    }
}