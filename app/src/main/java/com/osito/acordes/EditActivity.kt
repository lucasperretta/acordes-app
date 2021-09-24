package com.osito.acordes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.osito.acordes.databinding.ActivityEditBinding
import java.io.File

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("file")) {
            val file = intent.getStringExtra("file")
            binding.title.setText(file)
            binding.content.setText(readFromFile(file!!))
        }

        binding.save.setOnClickListener {
            Toast.makeText(this, "Archivo Guardado", Toast.LENGTH_SHORT).show()
            writeToFile(binding.title.text.toString(), binding.content.text.toString().replace("&nbsp!", " "))
            finish()
        }

        binding.delete.setOnClickListener {
            Toast.makeText(this, "Archivo Eliminado", Toast.LENGTH_SHORT).show()
            File(filesDir.absolutePath + "/" + binding.title.text).delete()
            finish()
        }
    }

}