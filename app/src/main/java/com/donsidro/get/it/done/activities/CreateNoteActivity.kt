package com.donsidro.get.it.done.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.donsidro.get.it.done.R
import com.donsidro.get.it.done.database.RoomDBHelper
import com.donsidro.get.it.done.databinding.ActivityCreateNoteBinding
import com.donsidro.get.it.done.databinding.ActivityMainBinding
import com.donsidro.get.it.done.modules.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding

    private var selectedColor: String = "#333333"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imageBack.setOnClickListener{
            onBackPressed()
        }

        binding.textDateTime.text = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(Date())

        binding.imageDone.setOnClickListener {
            saveNote()
        }

        initMiscellaneous()
    }

    private fun saveNote(){
        if (binding.inputNoteTitle.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            return
        }else if (binding.inputNoteSubtitle.text.toString().trim().isEmpty() &&
                binding.inputNote.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        var note = Note()
        note.title = binding.inputNoteTitle.text.toString()
        note.subTitle = binding.inputNoteSubtitle.text.toString()
        note.body = binding.inputNote.text.toString()
        note.dateTime = binding.textDateTime.text.toString()
        note.color = selectedColor

        CoroutineScope(Dispatchers.IO).launch {
            try {
                RoomDBHelper.instance.roomInsertNote(this@CreateNoteActivity, note)
                setResult(RESULT_OK, Intent())
                finish()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

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
    }

}