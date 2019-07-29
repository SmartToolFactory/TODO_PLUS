package com.smarttoolfactory.todoplus.di

import android.app.Application

import com.smarttoolfactory.todoplus.MyApplication

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


/*
 * [ActivityContributorModule] defines which Activities will have which modules and inject objects
 * If an Activity has any fragments it should add them via TasksFragmentContributorModule with @ContributesAndroidInjector
 * @ContributesAndroidInjector(modules = {TasksActivity.class, TasksFragmentContributorModule.class})
 */

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityContributorModule::class])
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: MyApplication)
}
