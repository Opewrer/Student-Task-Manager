package com.example.studenttaskmanager.ui

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
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
import java.util.Calendar

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private lateinit var adapter: TaskAdapter

    // Ініціалізація ViewModel через фабрику (виправляє помилку Unresolved reference 'viewModels')
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((requireActivity().application as TaskApp).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvTasksCalendar)

        // Налаштування RecyclerView
        adapter = TaskAdapter(
            onTaskClick = { task ->
            },
            onStatusChanged = { task, isCompleted ->
                viewModel.toggleTaskStatus(task, isCompleted)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Слухач зміни дати в календарі
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.loadTasksForDate(year, month, dayOfMonth)
        }

        // Спостереження за даними (використовуємо repeatOnLifecycle або запуск через viewLifecycleOwner)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedDateTasks.collect { tasks ->
                adapter.submitList(tasks)
            }
        }

        // Завантаження завдань на сьогодні при відкритті фрагмента
        val today = Calendar.getInstance()
        viewModel.loadTasksForDate(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
    }
}