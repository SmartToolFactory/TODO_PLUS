package com.smarttoolfactory.todoplus.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smarttoolfactory.todoplus.data.model.Task

/**
 * The Room Database that contains the Task table.
 *
 */
@Database(entities = [Task::class], version = DATABASE_VERSION, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
}

const val DATABASE_NAME = "tasks.db"
const val DATABASE_VERSION = 1