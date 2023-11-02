package com.example.project7_master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project7_master.databinding.NoteItemBinding
import kotlin.reflect.KFunction1

class NoteAdapter(
    private val clickListener: KFunction1<Note, Unit>
) : ListAdapter<Note, NoteAdapter.NoteItemViewHolder>(NoteDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        return NoteItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class NoteItemViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {

            fun inflateFrom(parent: ViewGroup): NoteItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
                return NoteItemViewHolder(binding)
            }

        }

        fun bind( // for each saved note, setup click listeners so can edit, delete.
            item: Note,
            clickListener: KFunction1<Note, Unit>
        ) {
            binding.note = item

            binding.root.setOnClickListener {
                clickListener(item)
            }
        }

    }
}
