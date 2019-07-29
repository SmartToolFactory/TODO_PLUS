package com.smarttoolfactory.todoplus.tasks

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.tasks.adapter.TasksAdapter


/*
    *** Bindings for List ***
 */
/**
 * [BindingAdapter]s for the [Task]s.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Task>) {
    println("🔥 TaskListBindings $items")
    (listView?.adapter as TasksAdapter)?.submitList(items)
}

/**
 * [BindingAdapter]s for setting text of list
 */
@BindingAdapter("app:completedTask")
fun setStyle(textView: TextView, enabled: Boolean) {
    if (enabled) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

/*
    *** Bindings for Map ***
 */