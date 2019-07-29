package com.smarttoolfactory.todoplus.di


import com.smarttoolfactory.todoplus.addedittask.AddEditTaskActivity
import com.smarttoolfactory.todoplus.tasks.TasksActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributorModule {

    /**
    Defines which fragments will be used by [TasksActivity]
     */
    @ContributesAndroidInjector(modules = [TasksFragmentContributorModule::class])
    abstract fun contributeTasksActivity(): TasksActivity

    /**
    Defines which fragments will be used by [AddEditTaskActivity]
     */
    @ContributesAndroidInjector(modules = [AddEditFragmentContributorModule::class])
    abstract fun contributeAddEditTasksActivity(): AddEditTaskActivity

}
