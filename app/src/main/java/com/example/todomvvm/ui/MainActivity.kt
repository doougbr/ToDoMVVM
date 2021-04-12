package com.example.todomvvm.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todomvvm.R
import com.example.todomvvm.data.Task
import com.example.todomvvm.databinding.ActivityMainBinding
import com.example.todomvvm.adapters.TaskListAdapter
import com.example.todomvvm.ui.tasks.TaskRepository
import com.example.todomvvm.ui.tasks.TaskViewModel
import com.example.todomvvm.ui.tasks.TaskViewModelFactory
import com.example.todomvvm.ui.tasks.TasksApplication

class MainActivity : AppCompatActivity(), TaskListAdapter.TaskItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private var dialog: Dialog? = null

    private val newTaskActivityRequestCode = 1
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TasksApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        Thread.sleep(1000)

        setTheme(R.style.Theme_ToDoMVVM)
        setContentView(view)

        val taskAdapter = TaskListAdapter(this)
        val recyclerView = binding.recyclerViewTasks
        recyclerView.adapter = taskAdapter
        val textViewNoTask = binding.textViewNoTasks
        val progressBar = binding.progressBar


        recyclerView.visibility = View.VISIBLE

        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)
        }

        // Add an observer on the LiveData returned by getWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.allTasks.observe(this) { tasks ->
            // Update the cached copy of the words in the adapter.

            taskAdapter.setTasks(tasks)

            tasks.let {
                taskAdapter.submitList(it)
                taskAdapter.registerAdapterDataObserver(object :
                    RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        super.onChanged()
                        checkEmpty()
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        checkEmpty()
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        super.onItemRangeRemoved(positionStart, itemCount)
                        checkEmpty()
                    }

                    fun checkEmpty() {
                        textViewNoTask.visibility =
                            (if (taskAdapter.itemCount == 0) View.VISIBLE else View.GONE)
                        binding.imageView.visibility =
                            (if (taskAdapter.itemCount == 0) View.VISIBLE else View.GONE)
                    }
                })
            }
        }

        val fab = binding.fabAddTask
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivityForResult(intent, newTaskActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newTaskActivityRequestCode && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val task = data?.getParcelableExtra<Task>(AddTaskActivity.EXTRA_REPLY)!!
                viewModel.insert(task)
//                data.getStringExtra(AddTaskActivity.EXTRA_REPLY)?.let { reply ->
//                    val task = Task(reply)!!
//                    viewModel.insert(task)
//                }
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onLongClick(task: Task) {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialogboxlayout)
        val deleteButton = dialog!!.findViewById<TextView>(R.id.textView_delete)
        dialog!!.show()

        deleteButton.setOnClickListener {
            viewModel.delete(task)
            dialog!!.dismiss()
        }
    }

    override fun onClick(task: Task) {
        viewModel.toggleCompleteState(task)
    }

    override fun onCheckClicked(task: Task) {
        viewModel.toggleCompleteState(task)
    }
}