package com.example.project7_master

import androidx.recyclerview.widget.DiffUtil

class NoteDiffUtil : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note)
            = (oldItem.noteId == newItem.noteId)
    override fun areContentsTheSame(oldItem: Note, newItem: Note) = (oldItem == newItem)
}