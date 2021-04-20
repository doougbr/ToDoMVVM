package com.example.todomvvm.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.todomvvm.R
import com.example.todomvvm.data.Task
import com.example.todomvvm.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setTheme(R.style.Theme_ToDoMVVM)
        setContentView(view)

        val editTextTaskTitle = binding.editTextTaskTitle
        val editTextTaskDesc = binding.editTextTaskDesc
        val textViewDate = binding.textViewDate

        val buttonAddTask = binding.imageButtonAddTask
        buttonAddTask.setOnClickListener {
            val intent = Intent()
            if (TextUtils.isEmpty(editTextTaskTitle.text)) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = Date()
                val dateToday = sdf.format(date)
                val task = Task(
                    task = editTextTaskTitle.text.toString(),
                    desc = editTextTaskDesc.text.toString(),
                    created = dateToday
                )

                textViewDate.text = dateToday
                intent.putExtra(EXTRA_REPLY, task)
                setResult(Activity.RESULT_OK, intent)

                finish()
            }
        }

        editTextTaskTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!editTextTaskTitle.text.isEmpty()) {
                    buttonAddTask.setColorFilter(Color.BLUE)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(editTextTaskTitle.text.isEmpty()) {
                    buttonAddTask.setColorFilter(Color.GRAY)
                }

            }

        })

    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.tasklistsql.REPLY"
    }
}