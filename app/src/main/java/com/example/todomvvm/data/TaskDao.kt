package com.example.todomvvm.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // declaring methods for database operations (queries) on task_table

    @Query("SELECT * FROM task_table") //Query that returns a list of words sorted in ascending order.
    fun getWords(): LiveData<MutableList<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

}

/* suspend functions belongs to kotlin coroutines. inserting something is an IO operation,
 which means it can take a while to be finished. So, suspend functions makes it run on a
 background thread.
*/