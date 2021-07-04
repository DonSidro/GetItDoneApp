package com.donsidro.get.it.done.ui.notesview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.donsidro.get.it.done.R
import com.donsidro.get.it.done.databinding.NotesFragmentBinding
import com.donsidro.get.it.done.ui.MainActivity
import com.donsidro.get.it.done.utils.OnSwipeTouchListener
import com.donsidro.get.it.done.utils.Status
import com.donsidro.get.it.done.utils.autoCleared
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class NotesFragment : Fragment(), NotesAdapter.NotesItemListener {

    private val TAG = "NotesFragment"
    private var binding: NotesFragmentBinding by autoCleared()
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var adapter: NotesAdapter
    var listChips : List<String> = listOf()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var fAuth: FirebaseAuth

    lateinit var profileView : FrameLayout

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
        profileView = mProfileMenuItem.actionView as FrameLayout

        profileView.setOnTouchListener( @SuppressLint("ClickableViewAccessibility")
        object : OnSwipeTouchListener(activity){
            override fun onSwipeDown() {
                super.onSwipeDown()
                Toast.makeText(activity, "Change Profile Down", Toast.LENGTH_LONG).show()
               /* if(usersToLogin.size > currentProfile){
                    currentProfile ++
                    auth = usersToLogin[currentProfile]?.auth!!
                    googleSignInClient = usersToLogin[currentProfile]?.googleSignInClient!!
                    gso = usersToLogin[usersToLogin]?.gso!!
                    if(auth!!.currentUser != null){
                        var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
                        activity?.let { it1 -> Glide.with(it1.applicationContext).asBitmap().load(
                            auth!!.currentUser?.photoUrl).into(imageView) }
                    }
                }else{
                    auth = null
                    googleSignInClient = null
                    gso = null
                    var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
                    imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
                }*/

            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                Toast.makeText(activity, "Change Profile Up", Toast.LENGTH_LONG).show()
                /*if(usersToLogin.size > currentProfile){
                    currentProfile --
                    auth = usersToLogin[currentProfile]?.auth!!
                    googleSignInClient = usersToLogin[currentProfile]?.googleSignInClient!!
                    gso = usersToLogin[usersToLogin]?.gso!!
                    if(auth!!.currentUser != null){
                        var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
                        activity?.let { it1 -> Glide.with(it1.applicationContext).asBitmap().load(
                            auth!!.currentUser?.photoUrl).into(imageView) }
                    }
                }else{
                    auth = null
                    googleSignInClient = null
                    gso = null
                    var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
                    imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
                }*/
            }

            override fun onClick() {
                Toast.makeText(activity, "Change Profile Click", Toast.LENGTH_LONG).show()
                if(fAuth?.currentUser != null){
                    fAuth!!.signOut()
                    var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
                    imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
                    googleSignInClient?.signOut()
                }else{
                    signIn()
                }
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

        if(fAuth?.currentUser != null){
            var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
            activity?.let { it1 -> Glide.with(it1.applicationContext).asBitmap().load(fAuth!!.currentUser?.photoUrl).into(imageView) }
        }


    }

    private val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result?.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)

                viewModel.signInWithGoogle(account!!).observe(viewLifecycleOwner, {
                    if (it.status == Status.SUCCESS) {
                        var imageView =  profileView.findViewById<CircleImageView>(R.id.imageNote)
                        activity?.let { it1 -> Glide.with(it1.applicationContext).asBitmap().load(fAuth!!.currentUser?.photoUrl).into(imageView) }
                    } else if (it.status == Status.ERROR) {

                    }
                })
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
            }
        }


    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        resultContract.launch(signInIntent)

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