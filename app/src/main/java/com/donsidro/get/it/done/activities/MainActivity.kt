package com.donsidro.get.it.done.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.donsidro.get.it.done.adapters.NoteAdapter
import com.donsidro.get.it.done.database.RoomDBHelper
import com.donsidro.get.it.done.databinding.ActivityMainBinding
import com.donsidro.get.it.done.modules.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var noteAdapter: NoteAdapter? = null
    private var noteList: MutableList<Note> = mutableListOf()

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


        binding.imageAddNoteMain.setOnClickListener {

            var intent = Intent(this, CreateNoteActivity::class.java)

            resultContract.launch(intent)

        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.notesRecyclerView.layoutManager = layoutManager
        noteAdapter = noteList?.let { NoteAdapter(it) }
        binding.notesRecyclerView.adapter = noteAdapter
    }

    private fun getNoteList(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(noteList!!.isEmpty()){
                    var notes = RoomDBHelper.instance.roomGetAllNotes(this@MainActivity)
                    noteList.addAll(notes)
                    noteAdapter?.notifyDataSetChanged()
                }else{
                    var note = RoomDBHelper.instance.roomGetNewNote(this@MainActivity)
                    noteList.add(0, note)
                    noteAdapter?.notifyDataSetChanged()
                }

                binding.notesRecyclerView.post { binding.notesRecyclerView.smoothScrollToPosition(0) }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }




}