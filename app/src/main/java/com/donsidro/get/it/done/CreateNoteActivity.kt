package com.donsidro.get.it.done

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donsidro.get.it.done.databinding.ActivityCreateNoteBinding
import com.donsidro.get.it.done.databinding.ActivityMainBinding

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
    }
}