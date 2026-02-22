package com.example.studenttaskmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.data.Task
import com.example.studenttaskmanager.databinding.ItemTaskBinding // Використовуємо згенерований клас
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onStatusChanged: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Використовуємо Binding для інфлейту розмітки
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title
                tvDueDate.text = "Due: ${formatDate(task.dueDate)}"

                // Скидаємо слухач перед зміною стану, щоб уникнути зациклення при скролі
                cbCompleted.setOnCheckedChangeListener(null)
                cbCompleted.isChecked = task.isCompleted

                // Встановлюємо колір пріоритету (використовуємо пріоритет як текст або фон)
                val colorRes = when (task.priority) {
                    "High" -> R.color.priority_high
                    "Medium" -> R.color.priority_medium
                    else -> R.color.priority_low
                }

                // priorityBadge — це TextView з вашого item_task.xml
                priorityBadge.text = task.priority
                priorityBadge.backgroundTintList = ContextCompat.getColorStateList(root.context, colorRes)

                // Обробка зміни статусу
                cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                    onStatusChanged(task, isChecked)
                }

                // Перехід до редагування
                root.setOnClickListener { onTaskClick(task) }
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
}