//package com.smarttoolfactory.todoplus.data.model
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import java.util.*
//
///**
// * @param id          id of the task
// * @param title       title of the task
// * @param description description of the task
// * @param isCompleted whether or not this task is completed
//
// */
//@Entity(tableName = "Tasks")
//data class TaskBackup(
//    @PrimaryKey @ColumnInfo(name = "task_id") var id: String = UUID.randomUUID().toString(),
//    @ColumnInfo(name = "title") var title: String = "Title is Empty",
//    @ColumnInfo(name = "description") var description: String = "Description is Empty",
//    @ColumnInfo(name = "tag") var tag: String,
//    @ColumnInfo(name = "completed") var isCompleted: Boolean,
//    @ColumnInfo(name = "due_date") var dueDate: Long,
//    @ColumnInfo(name = "due_date_request_code") var dueDateRequestCode: Long,
//    @ColumnInfo(name = "latitude") var latitude: Double,
//    @ColumnInfo(name = "longitude") var longitude: Double,
//    @ColumnInfo(name = "address") var address: String
//) {
//
//    val titleForList: String
//        get() = if (title.isNotEmpty()) title else description
//
//
//    val isActive
//        get() = !isCompleted
//
//    val isEmpty
//        get() = title.isEmpty() || description.isEmpty()
//
//    /**
//     * Check if there are any empty or null fields
//     */
////    val isContainEmptyProperty
////    get() = {
////
////    }
//
//    /*
//         @PrimaryKey @ColumnInfo(name = "task_id") var id: String = UUID.randomUUID().toString(),
//        @ColumnInfo(name = "title") var title: String = "Title is Empty",
//        @ColumnInfo(name = "description") var description: String = "Description is Empty",
//        @ColumnInfo(name = "tag") var tag: String = "No tag available",
//        @ColumnInfo(name = "completed") var isCompleted: Boolean = false,
//        @ColumnInfo(name = "due_date") var dueDate: Long = -1L,
//        @ColumnInfo(name = "due_date_request_code") var dueDateRequestCode: Long = -1L,
//        @ColumnInfo(name = "latitude") var latitude: Double = (-1).toDouble(),
//        @ColumnInfo(name = "longitude") var longitude: Double = (-1).toDouble(),
//        @ColumnInfo(name = "address") var address: String = ""
// */
//
//}
//
//
//
