package com.example.todomvvm.ui.tasks

import androidx.lifecycle.*
import com.example.todomvvm.data.Task
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Using LiveData and caching what allTasks returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allTasks: LiveData<MutableList<Task>> = repository.getAllTasksList()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun toggleCompleteState(task: Task) = viewModelScope.launch {
        task.completed = !task.completed // changing the current state
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun update(task:Task) = viewModelScope.launch {
        repository.update(task)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}