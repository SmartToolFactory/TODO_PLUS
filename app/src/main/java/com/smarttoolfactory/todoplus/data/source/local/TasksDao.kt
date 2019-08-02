package com.smarttoolfactory.todoplus.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.smarttoolfactory.todoplus.data.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Data Access Object for the tasks table.
 */
@Dao
interface TasksDao : BaseDao<Task> {

//    @Query("SELECT * from Tasks")
//    fun getTasksAndTas(): Flowable<List<TaskAllTags>>

    /**
     * Select all tasks from the tasks table. This method returns a [Flowable] which
     * emits values as long as observable changes or insert/ delete operation is invoked
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Tasks")
    fun getTasks(): Flowable<List<Task>>

    /**
     * Select all tasks with query keyword that match title, description or tags.
     * This method returns a [Flowable] which emits values as long as
     * observable changes or insert/ delete operation is invoked
     *
     * @return all tasks that match the query
     */

//     This query looks if its included
    @Query("SELECT * FROM Tasks WHERE title LIKE '%' || :taskQuery || '%' OR description LIKE '%' || :taskQuery || '%' OR tag LIKE '%' || :taskQuery || '%'")
    fun getTasks(taskQuery: String): Flowable<List<Task>>


    /**
     * Select all tasks with query keyword that match title, description or tags
     */
    @Query("SELECT * FROM Tasks")
    fun getTasksOnce(): Single<List<Task>>

    /**
     * Select all tasks with query keyword that match title, description or tags, and return
     * query results only once
     */
    @Query("SELECT * FROM Tasks WHERE title LIKE :taskQuery OR description LIKE :taskQuery OR tag LIKE :taskQuery")
    fun getTasksOnce(taskQuery: String): Single<List<Task>>

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Tasks WHERE task_id = :taskId")
    fun getTaskById(taskId: Long): Single<Task>


    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    fun updateTask(task: Task): Completable

    /**
     * Update the complete status of a task
     *
     * @param taskId    id of the task
     * @param completed status to be updated
     */
    @Query("UPDATE tasks SET completed = :completed WHERE task_id = :taskId")
    fun updateCompleted(taskId: Long, completed: Boolean): Completable

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM Tasks WHERE task_id = :taskId")
    fun deleteTaskById(taskId: Long): Completable

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Tasks")
    fun deleteTasks(): Completable

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.
     */
    @Query("DELETE FROM Tasks WHERE completed = 1")
    fun deleteCompletedTasks(): Completable


}