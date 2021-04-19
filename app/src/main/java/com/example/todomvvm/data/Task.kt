package com.example.todomvvm.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table") // room creates a sql lite table called task_table
@Parcelize  // allows to move object to another activity
data class Task(
    @ColumnInfo(name = "task") val task: String,
    @ColumnInfo(name = "desc") val desc: String? = null,
    @ColumnInfo(name = "created") val created: String? = null,
    @ColumnInfo(name = "completed") var completed: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable

