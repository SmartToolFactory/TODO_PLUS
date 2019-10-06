package com.smarttoolfactory.todoplus.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param id          id of the task
 * @param title       title of the task
 * @param description description of the task
 * @param isCompleted whether or not this task is completed

 */
@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") var id: Long = 0L,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "tag") var tag: String = "",
    @ColumnInfo(name = "completed") var isCompleted: Boolean = false,

    @ColumnInfo(name = "due_date_set") var dueDateSet: Boolean = false,
    @ColumnInfo(name = "due_date") var dueDate: Long = Long.MIN_VALUE,
    @ColumnInfo(name = "due_date_request_code") var dueDateRequestCode: Int = Int.MIN_VALUE,

    @ColumnInfo(name = "location_set") var locationSet: Boolean = false,
    @ColumnInfo(name = "latitude") var latitude: Double = Double.MIN_VALUE,
    @ColumnInfo(name = "longitude") var longitude: Double = Double.MIN_VALUE,
    @ColumnInfo(name = "address") var address: String = ""
) {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description


    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

}



