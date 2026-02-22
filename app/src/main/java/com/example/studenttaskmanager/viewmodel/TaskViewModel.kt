package com.example.studenttaskmanager.viewmodel

import androidx.lifecycle.*
import com.example.studenttaskmanager.data.Task
import com.example.studenttaskmanager.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks

    private val _selectedDateTasks = MutableStateFlow<List<Task>>(emptyList())
    val selectedDateTasks: StateFlow<List<Task>> = _selectedDateTasks

    init {
        viewModelScope.launch {
            repository.allTasks.collect { _allTasks.value = it }
        }
    }

    fun addTask(title: String, desc: String, date: Long, priority: String) {
        viewModelScope.launch {
            repository.insert(Task(title = title, description = desc, dueDate = date, priority = priority))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun toggleTaskStatus(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.update(task.copy(isCompleted = isCompleted))
        }
    }

    fun loadTasksForDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth, 0, 0, 0)
        val start = calendar.timeInMillis
        calendar.set(year, month, dayOfMonth, 23, 59, 59)
        val end = calendar.timeInMillis

        viewModelScope.launch {
            repository.getTasksByDate(start, end).collect {
                _selectedDateTasks.value = it
            }
        }
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