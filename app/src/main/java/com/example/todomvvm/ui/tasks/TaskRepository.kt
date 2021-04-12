package com.example.todomvvm.ui.tasks

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.todomvvm.data.Task
import com.example.todomvvm.data.TaskDao
import kotlinx.coroutines.flow.Flow


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class TaskRepository(private val taskDao: TaskDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTasks: LiveData<MutableList<Task>> = taskDao.getWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    fun getAllTasksList(): LiveData<MutableList<Task>>{
        return allTasks
    }

}