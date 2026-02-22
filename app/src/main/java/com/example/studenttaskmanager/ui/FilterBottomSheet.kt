package com.example.studenttaskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.studenttaskmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_filter_bottom_sheet, container, false)

        val btnApply = view.findViewById<Button>(R.id.btnApply)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rgStatus)
        val rgPriority = view.findViewById<RadioGroup>(R.id.rgPriority)

        btnApply.setOnClickListener {
            // Отримуємо вибраний статус
            val status = when (rgStatus.checkedRadioButtonId) {
                R.id.rbCompleted -> "Completed"
                R.id.rbIncomplete -> "Incomplete"
                else -> "All"
            }

            // Отримуємо вибраний пріоритет
            val priority = when (rgPriority.checkedRadioButtonId) {
                R.id.rbHigh -> "High"
                R.id.rbMedium -> "Medium"
                R.id.rbLow -> "Low"
                else -> "All"
            }

            // Передаємо результат через Fragment Result API
            val result = Bundle().apply {
                putString("status", status)
                putString("priority", priority)
            }
            parentFragmentManager.setFragmentResult("filter_request", result)
            dismiss()
        }

        return view
    }
}