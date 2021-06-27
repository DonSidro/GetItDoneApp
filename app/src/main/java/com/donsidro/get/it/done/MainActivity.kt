package com.donsidro.get.it.done

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.donsidro.get.it.done.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->

        if(result?.resultCode == Activity.RESULT_OK){

        }


    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.imageAddNoteMain.setOnClickListener {

            var intent = Intent(this, CreateNoteActivity::class.java)

            resultContract.launch(intent)

        }

    }




}