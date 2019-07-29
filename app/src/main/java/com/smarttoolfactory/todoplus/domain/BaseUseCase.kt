package com.smarttoolfactory.todoplus.domain

import io.reactivex.disposables.CompositeDisposable


abstract class BaseUseCase {

    /**
     * This method is required to dispose RxJava Observables
     */
    abstract fun dispose()
}