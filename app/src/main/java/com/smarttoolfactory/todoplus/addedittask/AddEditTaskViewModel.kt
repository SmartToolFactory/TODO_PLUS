package com.smarttoolfactory.todoplus.addedittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.domain.AddEditTaskUseCase
import javax.inject.Inject

class AddEditTaskViewModel @Inject
constructor(private val repository: TasksRepository,
            private val addEditUseCase: AddEditTaskUseCase
) : ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

}