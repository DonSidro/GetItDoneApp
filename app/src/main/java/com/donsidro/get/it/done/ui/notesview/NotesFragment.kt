package com.donsidro.get.it.done.ui.notesview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.donsidro.get.it.done.R
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.databinding.NotesFragmentBinding
import com.donsidro.get.it.done.utils.OnSwipeTouchListener
import com.donsidro.get.it.done.utils.autoCleared
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

@AndroidEntryPoint
class NotesFragment : Fragment(), NotesAdapter.NotesItemListener {

    private val TAG = "NotesFragment"
    private var binding: NotesFragmentBinding by autoCleared()
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var adapter: NotesAdapter
    var listChips : List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupChips()
        setHasOptionsMenu(true)
    }

    private fun setupChips() {
        var lister = CompoundButton.OnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
            if (b){
                listChips += if (compoundButton.text.equals("Image")) "_chip_image_" else "_chip_url_"
            }else{
                listChips -= if (compoundButton.text.equals("Image")) "_chip_image_" else "_chip_url_"
            }
            viewModel.searchNoteChangedFilter(listChips)
        }
        binding.imageChip.setOnCheckedChangeListener(lister)
        binding.URLChip.setOnCheckedChangeListener(lister)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSearchMenuItem = menu.findItem(R.id.action_search)
        val mProfileMenuItem = menu.findItem(R.id.menu_profile)
        val searchView = mSearchMenuItem.actionView as SearchView
        val profileView = mProfileMenuItem.actionView as FrameLayout

        profileView.setOnTouchListener( @SuppressLint("ClickableViewAccessibility")
        object : OnSwipeTouchListener(activity){
            override fun onSwipeDown() {
                super.onSwipeDown()
                Toast.makeText(activity, "Change Profile Down", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                Toast.makeText(activity, "Change Profile Up", Toast.LENGTH_LONG).show()
            }

            override fun onClick() {
                Toast.makeText(activity, "Change Profile Click", Toast.LENGTH_LONG).show()
                super.onClick()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchNoteChanged(newText)
                return false
            }
        })


    }


        private fun setupRecyclerView() {

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        adapter = NotesAdapter(this)
        binding.notesRecyclerView.layoutManager = layoutManager
        binding.notesRecyclerView.adapter = adapter

        }

    private fun setupObservers() {
       viewModel.notes.observe(viewLifecycleOwner, Observer {
           adapter.setItems(ArrayList(it))
        })
    }

    override fun onClickedNote(noteId: Int) {
        findNavController().navigate(
            R.id.action_notesFragment_to_notesDetailFragment,
            bundleOf("id" to noteId)
        )
    }
}