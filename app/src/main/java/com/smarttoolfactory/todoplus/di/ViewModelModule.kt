package com.smarttoolfactory.todoplus.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.todoplus.addedittask.AddEditTaskViewModel
import com.smarttoolfactory.todoplus.tasks.TaskListViewModel
import com.smarttoolfactory.todoplus.viewmodel.CustomViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 *
 * This module is used for retrieving ViewModels using [CustomViewModelFactory], viewModels are put into map
 * via @IntoMap and @ViewModelKey annotations
 *
 * IntoMap creates a key-value pair to get relevant ViewModel. Key is the class name of ViewModel
 * and value is the ViewModel itself
 */
@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    abstract fun bindTaskListViewModel(tasksViewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditTaskViewModel::class)
    abstract fun bindAddEditTaskViewModel(tasksViewModel: AddEditTaskViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory
}
