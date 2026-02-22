package com.example.studenttaskmanager.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.TaskApp
import com.example.studenttaskmanager.viewmodel.TaskViewModel
import com.example.studenttaskmanager.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    // Отримуємо доступ до ViewModel через фабрику, передаючи репозиторій з TaskApp
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((requireActivity().application as TaskApp).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvTasks)

        // Ініціалізуємо адаптер, який ми створили раніше
        val adapter = TaskAdapter(
            onTaskClick = { task ->
                // Логіка для редагування завдання (наприклад, відкриття AddEditTaskActivity)
            },
            onStatusChanged = { task, isCompleted ->
                // Оновлюємо статус завдання в базі даних через ViewModel
                viewModel.toggleTaskStatus(task, isCompleted)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Підписуємося на оновлення списку завдань з бази даних
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allTasks.collect { tasks ->
                // Передаємо новий список в адаптер (DiffUtil зробить плавну анімацію)
                adapter.submitList(tasks)
            }
        }
    }
}