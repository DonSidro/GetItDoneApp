package com.donsidro.get.it.done.ui.notesdetails

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.donsidro.get.it.done.R
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.databinding.CreateNoteFragmentBinding
import com.donsidro.get.it.done.utils.autoCleared
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class NotesDetailFragment : Fragment() {

    private var binding: CreateNoteFragmentBinding by autoCleared()
    private val viewModel: NoteDetailViewModel by viewModels()

    @Inject
    lateinit var fAuth: FirebaseAuth
    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    private var selectedColor: String = "#333333"
    private var selectedImagePath : String = ""
    private var editNote: Note? = null

    private val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->

        if(result?.resultCode == Activity.RESULT_OK){
            val imageUri = result.data?.data

            if(imageUri != null){
                val inputStream = activity?.contentResolver?.openInputStream(imageUri)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imageNote.setImageBitmap(bitmap)
                binding.imageNote.visibility = View.VISIBLE
                selectedImagePath = getPathFromUri(imageUri)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateNoteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("id")?.let { viewModel.start(it) }
        setupViews()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.note.observe(viewLifecycleOwner, {
            bindNote(it)
        })
    }

    private fun bindNote(editNote: Note){
        this.editNote = editNote
        binding.inputNote.setText(editNote?.body)
        binding.inputNoteSubtitle.setText(editNote?.subTitle)
        binding.inputNoteTitle.setText(editNote?.title)
        binding.textDateTime.text = editNote?.dateTime
        selectedColor = editNote?.color.toString()

        if(!editNote?.imagePath.isNullOrEmpty()){
            binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(editNote!!.imagePath))
            binding.imageNote.visibility = View.VISIBLE
            selectedImagePath = editNote!!.imagePath.toString()
        }
        if(!editNote?.webLink.isNullOrEmpty()){
            binding.textWebURL.text = editNote!!.webLink
            binding.layoutWebURL.visibility = View.VISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.toolbar_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSaveMenuItem = menu.findItem(R.id.menu_done)

        mSaveMenuItem.setOnMenuItemClickListener {
            saveNote()
            activity?.onBackPressed()
            return@setOnMenuItemClickListener false
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun saveNote(){
            if (binding.inputNoteTitle.text.toString().trim().isEmpty()){
                Toast.makeText(activity, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
                return
            }else if (binding.inputNoteSubtitle.text.toString().trim().isEmpty() &&
                binding.inputNote.text.toString().trim().isEmpty()){
                Toast.makeText(activity, "Note can't be empty!", Toast.LENGTH_SHORT).show()
                return
            }

            var note = Note()
            note.title = binding.inputNoteTitle.text.toString()
            note.subTitle = binding.inputNoteSubtitle.text.toString()
            note.body = binding.inputNote.text.toString()
            note.dateTime = binding.textDateTime.text.toString()
            note.color = selectedColor
            note.imagePath = selectedImagePath
            note.randomFirebaseID = randomID()
            if (binding.layoutWebURL.visibility == View.VISIBLE)
                note.webLink = binding.textWebURL.text.toString()

            if(editNote != null){
                note.id = editNote!!.id
            }

            firebaseFirestore.collection("Users").document(fAuth.uid.toString())
                .collection("Notes")
                .document(note.randomFirebaseID.toString()).set(note)

            viewModel.saveNote(note)

    }

    private fun randomID(): String = List(16) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")

    private fun setupViews(){
        binding.textDateTime.text = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(Date())
        initMiscellaneous()

    }


    private fun getPathFromUri(uri: Uri) : String{
        var filePath : String = ""
        var cursor = activity?.contentResolver?.query(uri, null, null, null,null)
        if(cursor == null){
            filePath = uri.path.toString()
        }else{
            cursor.moveToNext()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private fun initMiscellaneous(){
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMiscellaneous.layoutMiscellaneous)
        binding.layoutMiscellaneous.layoutMiscellaneous.setOnClickListener {
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            }
        }
        setIndicatorColor()
        setupColorPickerClicks()
    }

    private fun setIndicatorColor(){
        var gradientDrawable = binding.viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(parseColor(selectedColor))

    }

    private fun setupColorPickerClicks(){

        binding.layoutMiscellaneous.imageColor1.setOnClickListener {
            selectedColor = "#333333"
            binding.layoutMiscellaneous.imageColor1.setImageResource(R.drawable.ic_done)
            binding.layoutMiscellaneous.imageColor2.setImageResource(0)
            binding.layoutMiscellaneous.imageColor3.setImageResource(0)
            binding.layoutMiscellaneous.imageColor4.setImageResource(0)
            binding.layoutMiscellaneous.imageColor5.setImageResource(0)
            setIndicatorColor()
        }
        binding.layoutMiscellaneous.imageColor2.setOnClickListener {
            selectedColor = "#FDBE3B"
            binding.layoutMiscellaneous.imageColor1.setImageResource(0)
            binding.layoutMiscellaneous.imageColor2.setImageResource(R.drawable.ic_done)
            binding.layoutMiscellaneous.imageColor3.setImageResource(0)
            binding.layoutMiscellaneous.imageColor4.setImageResource(0)
            binding.layoutMiscellaneous.imageColor5.setImageResource(0)
            setIndicatorColor()

        }
        binding.layoutMiscellaneous.imageColor3.setOnClickListener {
            selectedColor = "#FF4842"
            binding.layoutMiscellaneous.imageColor1.setImageResource(0)
            binding.layoutMiscellaneous.imageColor2.setImageResource(0)
            binding.layoutMiscellaneous.imageColor3.setImageResource(R.drawable.ic_done)
            binding.layoutMiscellaneous.imageColor4.setImageResource(0)
            binding.layoutMiscellaneous.imageColor5.setImageResource(0)
            setIndicatorColor()

        }
        binding.layoutMiscellaneous.imageColor4.setOnClickListener {
            selectedColor = "#000000"
            binding.layoutMiscellaneous.imageColor1.setImageResource(0)
            binding.layoutMiscellaneous.imageColor2.setImageResource(0)
            binding.layoutMiscellaneous.imageColor3.setImageResource(0)
            binding.layoutMiscellaneous.imageColor4.setImageResource(R.drawable.ic_done)
            binding.layoutMiscellaneous.imageColor5.setImageResource(0)
            setIndicatorColor()

        }
        binding.layoutMiscellaneous.imageColor5.setOnClickListener {
            selectedColor = "#3A52FC"
            binding.layoutMiscellaneous.imageColor1.setImageResource(0)
            binding.layoutMiscellaneous.imageColor2.setImageResource(0)
            binding.layoutMiscellaneous.imageColor3.setImageResource(0)
            binding.layoutMiscellaneous.imageColor4.setImageResource(0)
            binding.layoutMiscellaneous.imageColor5.setImageResource(R.drawable.ic_done)
            setIndicatorColor()

        }

        if(!editNote?.color.isNullOrEmpty()){
            when (editNote?.color.toString()) {
                "#333333" -> binding.layoutMiscellaneous.imageColor1.performClick()
                "#FDBE3B" -> binding.layoutMiscellaneous.imageColor2.performClick()
                "#FF4842" -> binding.layoutMiscellaneous.imageColor3.performClick()
                "#000000" -> binding.layoutMiscellaneous.imageColor4.performClick()
                "#3A52FC" -> binding.layoutMiscellaneous.imageColor5.performClick()

            }
        }

        binding.layoutMiscellaneous.layoutAddImage.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMiscellaneous.layoutMiscellaneous)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            when (PackageManager.PERMISSION_GRANTED) {
                activity?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1.applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } -> {
                    selectImage();
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

        }

        binding.layoutMiscellaneous.layoutAddUrl.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMiscellaneous.layoutMiscellaneous)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddURLDialog()
        }
    }

    private fun selectImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultContract.launch(gallery)
    }

    private fun showAddURLDialog(){

        var builder = AlertDialog.Builder(activity)
        var view = LayoutInflater.from(activity).inflate(R.layout.layout_add_url, activity?.findViewById(R.id.layoutAddUrlContainer))

        builder?.setView(view)

        var alert = builder?.create()

        if(alert?.window != null){
            alert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        var inputURL = view.findViewById<EditText>(R.id.inputURL)
        inputURL.requestFocus()

        view.findViewById<TextView>(R.id.textAdd).setOnClickListener {
            if(inputURL.text.toString().trim().isEmpty()){
                Toast.makeText(activity, "Enter URL", Toast.LENGTH_SHORT).show()
            }else if(!Patterns.WEB_URL.matcher(inputURL.text.toString()).matches()){
                Toast.makeText(activity, "Enter valid URL", Toast.LENGTH_SHORT).show()
            }else{
                binding.textWebURL.text = inputURL.text
                binding.layoutWebURL.visibility = View.VISIBLE
                alert?.dismiss()
            }
        }

        view.findViewById<TextView>(R.id.textCancel).setOnClickListener {
            alert?.dismiss()
        }

        alert?.show()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                selectImage()
            } else {
                Toast.makeText(activity,"Permission Denied!",Toast.LENGTH_SHORT).show()
            }
        }



}