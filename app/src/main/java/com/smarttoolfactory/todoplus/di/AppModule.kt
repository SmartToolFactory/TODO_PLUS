package com.smarttoolfactory.todoplus.di


import android.app.Application
import androidx.room.Room
import com.smarttoolfactory.todoplus.data.TasksDataSource
import com.smarttoolfactory.todoplus.data.TasksRepository
import com.smarttoolfactory.todoplus.data.TasksRepositoryImpl
import com.smarttoolfactory.todoplus.data.source.local.DATABASE_NAME
import com.smarttoolfactory.todoplus.data.source.local.LocalTaskDataSource
import com.smarttoolfactory.todoplus.data.source.local.TasksDao
import com.smarttoolfactory.todoplus.data.source.local.ToDoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    /*
        *** Database Injections ***
     */

    /**
     * Provide copy of database
     */
    @Singleton
    @Provides
    fun provideDatabase(application: Application): ToDoDatabase {
        return Room.databaseBuilder(
                application,
                ToDoDatabase::class.java,
                DATABASE_NAME
        ).build()
    }

    /**
     * Provide Data Access Object for querying database
     */
    @Singleton
    @Provides
    fun provideTasksDao(toDoDatabase: ToDoDatabase): TasksDao {
        return toDoDatabase.taskDao()
    }

    @Singleton
    @Provides
    fun provideTasksDataSource(tasksDao: TasksDao): TasksDataSource {
        return LocalTaskDataSource(tasksDao)
    }

    @Singleton
    @Provides
    fun provideRepository(tasksDataSource: TasksDataSource): TasksRepository {
        return TasksRepositoryImpl(tasksDataSource)
    }
}
