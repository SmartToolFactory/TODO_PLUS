package com.smarttoolfactory.todoplus.di


import com.smarttoolfactory.todoplus.addedittask.AddEditTaskFragment
import com.smarttoolfactory.todoplus.addedittask.AddLocationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * TasksFragmentContributorModule is used inside ActivityContributorModule
 * With @ContributesAndroidInjector(modules = TasksFragmentContributorModule.class)
 * defines which module will be used to inject objects to declared fragments
 *
 * This ContributorModule is used by [AddEditTaskActivity]
 */
@Module
abstract class AddEditFragmentContributorModule {

    @ContributesAndroidInjector
    abstract fun contributeAddEditTaskFragment(): AddEditTaskFragment

    @ContributesAndroidInjector
    abstract fun contributeAddLocationFragmentFragment(): AddLocationFragment

}

