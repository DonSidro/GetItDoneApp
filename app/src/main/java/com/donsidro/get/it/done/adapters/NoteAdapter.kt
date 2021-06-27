package com.donsidro.get.it.done.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.donsidro.get.it.done.R
import com.donsidro.get.it.done.databinding.ActivityMainBinding
import com.donsidro.get.it.done.databinding.ItemConainerNoteBinding
import com.donsidro.get.it.done.listeners.NotesListener
import com.donsidro.get.it.done.modules.Note

class NoteAdapter (private val dataSet: List<Note>, val notesListener: NotesListener) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>(){


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemConainerNoteBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
        viewHolder.setClick(dataSet[position], position)

    }

    inner class ViewHolder(private val binding: ItemConainerNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            with(binding) {
                binding.textTitle.text = item.title
                if(item.subTitle.toString().trim().isEmpty()){
                    binding.textSubtitle.visibility = View.GONE
                }else{
                    binding.textSubtitle.text = item.subTitle
                }
                binding.textDateTime.text = item.dateTime


                var gradientDrawable = binding.layoutNote.background as GradientDrawable
                if(item.color != null){
                    gradientDrawable.setColor(Color.parseColor(item.color))
                }else{
                    gradientDrawable.setColor(Color.parseColor("#333333"))
                }

                if(item.imagePath != null) {
                    binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(item.imagePath))
                    binding.imageNote.visibility = View.VISIBLE
                }else{
                    binding.imageNote.visibility = View.GONE
                }

            }

        }
        fun setClick(item: Note, position: Int){
            binding.layoutNote.setOnClickListener {
                notesListener.onNoteClicked(item, position)
            }
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}