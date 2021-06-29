package com.donsidro.get.it.done.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.donsidro.get.it.done.adapters.NoteAdapter
import com.donsidro.get.it.done.database.RoomDBHelper
import com.donsidro.get.it.done.databinding.ActivityMainBinding
import com.donsidro.get.it.done.listeners.NotesListener
import com.donsidro.get.it.done.modules.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity(), NotesListener {

    private val TAG = "MainActivity"
    private var noteAdapter: NoteAdapter? = null
    private var noteList: MutableList<Note> = mutableListOf()
    private var editMode : Boolean = false
    private var editPosition: Int? = null

    private val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result?.resultCode == Activity.RESULT_OK){
            getNoteList()
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getNoteList()


        binding.fabMainAddNote.setOnClickListener {

            var intent = Intent(this, CreateNoteActivity::class.java)
            resultContract.launch(intent)
        }

        setupRecyclerView()

        setupQuickActions()
    }

    private fun setupQuickActions() {

        binding.bottomAppBar.menu[0].setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "add_note is clicked!", Toast.LENGTH_SHORT).show()
            return@setOnMenuItemClickListener true
        }
        binding.bottomAppBar.menu[1].setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "add_image is clicked!", Toast.LENGTH_SHORT).show()
            return@setOnMenuItemClickListener true
        }
        binding.bottomAppBar.menu[2].setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "add_URL is clicked!", Toast.LENGTH_SHORT).show()
            return@setOnMenuItemClickListener true
        }
    }

    var mIth = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            or ItemTouchHelper.START or ItemTouchHelper.END ,
            0
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos: Int = viewHolder.bindingAdapterPosition
                val toPos: Int = target.bindingAdapterPosition

                Collections.swap(noteList, fromPos, toPos)
                if (fromPos < toPos) {
                    for (i in fromPos until toPos) {
                        Collections.swap(noteList, i, i + 1)
                    }
                } else {
                    for (i in fromPos downTo toPos + 1) {
                        Collections.swap(noteList, i, i - 1)
                    }
                }
                noteAdapter?.notifyItemMoved(fromPos, toPos)

                CoroutineScope(Dispatchers.IO).launch {
                    for (note in noteList) {
                        note.position = noteList.indexOf(note)
                        RoomDBHelper.instance.roomUpdateNote(applicationContext, note)
                    }
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }


        })


    private fun setupRecyclerView(){
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.notesRecyclerView.layoutManager = layoutManager
        noteAdapter = noteList?.let { NoteAdapter(it, this) }
        binding.notesRecyclerView.adapter = noteAdapter

        //mIth.attachToRecyclerView(binding.notesRecyclerView)

    }

    private fun getNoteList(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(!editMode) {
                    if (noteList!!.isEmpty()) {
                        var notes = RoomDBHelper.instance.roomGetAllNotes(this@MainActivity)
                        noteList.addAll(notes)
                        noteAdapter?.notifyDataSetChanged()
                    } else {
                        var note = RoomDBHelper.instance.roomGetNewNote(this@MainActivity)
                        noteList.add(0, note)
                        noteAdapter?.notifyItemInserted(0)
                    }
                }else{
                    var notes = RoomDBHelper.instance.roomGetAllNotes(this@MainActivity)
                    editPosition?.let { noteList.removeAt(it) }
                    noteList.add(editPosition!!, notes[editPosition!!])
                    noteAdapter?.notifyItemChanged(editPosition!!)
                }

                binding.notesRecyclerView.post { binding.notesRecyclerView.smoothScrollToPosition(0) }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onNoteClicked(note: Note, postion: Int) {
        editPosition = postion;
        editMode = true
        var intent = Intent(this, CreateNoteActivity::class.java)
        intent.putExtra("isViewOrUpdate", true)
        intent.putExtra("note", note)

        resultContract.launch(intent)
    }

}