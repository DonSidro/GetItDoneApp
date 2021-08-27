//package com.donsidro.get.it.done.ui.notesview
//
//import android.annotation.SuppressLint
//import android.graphics.BitmapFactory
//import android.graphics.Color
//import android.graphics.drawable.GradientDrawable
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.donsidro.get.it.done.data.entities.Note
//import com.donsidro.get.it.done.utils.DiffUtilNote
//
//class NotesAdapter (private val listener: NotesItemListener) : RecyclerView.Adapter<NotesViewHolder>() {
//
//    interface NotesItemListener {
//        fun onClickedNote(noteId: Int)
//    }
//
//    private var items = ArrayList<Note>()
//
//    fun setItems(items: ArrayList<Note>) {
//        val noteDiffUtil= DiffUtilNote(this.items, items)
//        val noteDiffResult = DiffUtil.calculateDiff(noteDiffUtil)
//        this.items = items
//        noteDiffResult.dispatchUpdatesTo(this)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
//        val binding: ItemConainerNoteBinding = ItemConainerNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return NotesViewHolder(binding, listener)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) = holder.bind(items[position])
//}
//
//
//class NotesViewHolder(private val itemBinding: ItemConainerNoteBinding, private val listener: NotesAdapter.NotesItemListener) : RecyclerView.ViewHolder(itemBinding.root),
//    View.OnClickListener {
//
//    private lateinit var note: Note
//
//    init {
//        itemBinding.root.setOnClickListener(this)
//    }
//
//    @SuppressLint("SetTextI18n")
//    fun bind(item: Note) {
//        note = item
//        itemBinding.textTitle.text = item.title
//        if(item.subTitle.toString().trim().isEmpty()){
//            itemBinding.textSubtitle.visibility = View.GONE
//        }else{
//            itemBinding.textSubtitle.text = item.subTitle
//        }
//        itemBinding.textDateTime.text = item.dateTime
//
//
//        val gradientDrawable = itemBinding.layoutNote.background as GradientDrawable
//        if(item.color != null){
//            gradientDrawable.setColor(Color.parseColor(item.color))
//        }else{
//            gradientDrawable.setColor(Color.parseColor("#333333"))
//        }
//
//        if(item.imagePath != null) {
//            itemBinding.imageNote.setImageBitmap(BitmapFactory.decodeFile(item.imagePath))
//            itemBinding.imageNote.visibility = View.VISIBLE
//        }else{
//            itemBinding.imageNote.visibility = View.GONE
//        }
//
//    }
//
//    override fun onClick(v: View?) {
//        listener.onClickedNote(note.id)
//    }
//}
