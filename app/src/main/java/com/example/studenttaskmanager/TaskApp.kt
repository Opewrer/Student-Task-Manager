package com.example.studenttaskmanager

import android.app.Application
import com.example.studenttaskmanager.data.TaskDatabase
import com.example.studenttaskmanager.data.TaskRepository

class TaskApp : Application() {
    val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }

    // Порожній onCreate видалено
}