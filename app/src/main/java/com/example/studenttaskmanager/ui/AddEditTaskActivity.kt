package com.example.studenttaskmanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.TaskApp
import com.example.studenttaskmanager.data.Task
import com.example.studenttaskmanager.viewmodel.TaskViewModel
import com.example.studenttaskmanager.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {

    // Ініціалізація ViewModel через фабрику
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskApp).repository)
    }

    private var selectedDate: Long = System.currentTimeMillis()
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        val etTitle = findViewById<EditText>(R.id.etTaskTitle)
        val etDesc = findViewById<EditText>(R.id.etDescription)
        val btnDate = findViewById<Button>(R.id.btnSelectDate)
        val spinnerPriority = findViewById<Spinner>(R.id.spinnerPriority)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        // Налаштування Spinner для вибору пріоритету
        val priorities = arrayOf("High", "Medium", "Low")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority.adapter = adapter


        taskId = intent.getIntExtra("TASK_ID", -1)
        if (taskId != -1) {
            // Якщо редагуємо, можна заповнити поля даними (логіка отримання об'єкта)
            etTitle.setText(intent.getStringExtra("TASK_TITLE"))
            etDesc.setText(intent.getStringExtra("TASK_DESC"))
            // Додаткова логіка для встановлення дати та пріоритету
        }

        // Вибір дати (DatePickerDialog)
        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.timeInMillis
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                btnDate.text = sdf.format(calendar.time)
            }

            DatePickerDialog(
                this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Збереження завдання
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val priority = spinnerPriority.selectedItem.toString()

            if (title.isNotEmpty()) {
                if (taskId == -1) {
                    viewModel.addTask(title, desc, selectedDate, priority)
                } else {
                    viewModel.updateTask(Task(taskId, title, desc, selectedDate, priority))
                }
                finish()
            } else {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}